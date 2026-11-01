package pizza.order;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import pizza.customer.Address;
import pizza.customer.Customer;
import pizza.customer.CustomerService;
import pizza.delivery.DeliveryTimeClient;
import pizza.product.ProductService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    //
    // injected beans
    //

    private final OrderRepository orderRepository;

    private final OrderProperties orderProperties;

    private final CustomerService customerService;

    private final ProductService productService;

    private final DeliveryTimeClient deliveryTimeClient;

    //
    // constructors and setup
    //

    public OrderService(OrderRepository orderRepository, OrderProperties orderProperties, CustomerService customerService, ProductService productService, DeliveryTimeClient deliveryTimeClient) {
        this.orderRepository = orderRepository;
        this.orderProperties = orderProperties;
        this.customerService = customerService;
        this.productService = productService;
        this.deliveryTimeClient = deliveryTimeClient;
    }

    @PostConstruct
    public void dumpConfiguration() {
        System.out.printf("""
                Using configuration:
                - deliveryTimeInMinutes=%d
                - discountDays=%s
                - discountRate=%2.2f
                %n""", orderProperties.deliveryTimeInMinutes(), orderProperties.discountDays(), orderProperties.discountRate());
    }

    //
    // business logic
    //

    /**
     * Places an order for a Customer identified by his/her phone number.
     * <p>
     * The kind and number of products ordered are given in the <code>productQuantities</code> map.
     *
     * @param phoneNumber       required argument, the phone number to identify the Customer with
     * @param productQuantities required argument, a mapping of product-ids to their desired
     *                          quantities
     * @return the {@link Order} entity which contains the total price and estimated time of
     * delivery
     */
    @Transactional
    public Order placeOrder(String phoneNumber, Map<String, Integer> productQuantities) {
        // make sure customer exists -- throws exception if it doesn't
        Customer customer = this.customerService.getCustomerByPhoneNumber(phoneNumber);

        // Übung a) bis c): der Versuch zählt in einer eigenen Transaktion und übersteht so einen
        // Rollback der Bestellung; der Aufruf muss dafür über den Proxy einer anderen Bean gehen (Bonus d)
        this.customerService.increaseOrderCount(customer.getId());

        // calculate total price
        BigDecimal totalPrice = this.productService.getTotalPrice(productQuantities);

        // discounts
        double todaysDiscountRate = getTodaysDiscountRate();
        BigDecimal discountedTotalPrice = totalPrice
                .multiply(BigDecimal.valueOf(1.0 - todaysDiscountRate / 100.0))
                .setScale(2, RoundingMode.HALF_UP);
        System.out.println("Reducing price of order from " + totalPrice + " to " + discountedTotalPrice
                + " due to today's discount of " + todaysDiscountRate + "%");

        // create order
        Order order = new Order(
                customer,
                discountedTotalPrice,
                LocalDateTime.now().plusMinutes(getDeliveryTimeInMinutes(customer)));

        // persist and return it
        return this.orderRepository.save(order);
    }

    // exercise a): ask the delivery time service, b): fall back to the property if it does not answer
    private int getDeliveryTimeInMinutes(Customer customer) {
        Address address = customer.getAddress();
        if (address == null || address.getPostalCode() == null) {
            return this.orderProperties.deliveryTimeInMinutes();
        }
        try {
            return this.deliveryTimeClient.getDeliveryTimeInMinutes(address.getPostalCode());
        } catch (RestClientException e) {
            // 404, service unreachable, timeout: the order must not fail because of it
            log.warn("No estimate from the delivery time service for {}, using {} minutes: {}",
                    address.getPostalCode(), this.orderProperties.deliveryTimeInMinutes(), e.toString());
            return this.orderProperties.deliveryTimeInMinutes();
        }
    }

    private double getTodaysDiscountRate() {
        String nameOfDayOfWeek = LocalDate.now().getDayOfWeek().name();
        if (this.orderProperties.discountDays().contains(nameOfDayOfWeek)) {
            return this.orderProperties.discountRate();
        } else {
            return 0.0d;
        }
    }

    public Iterable<Order> getOrders() {
        return this.orderRepository.findAll();
    }

    public List<Order> getOrdersOfCustomer(String phoneNumber) {
        return this.orderRepository.findByCustomerPhoneNumber(phoneNumber);
    }

    public BigDecimal getTotalRevenueOfCustomer(String phoneNumber) {
        return this.orderRepository.sumTotalPriceByPhoneNumber(phoneNumber);
    }
}
