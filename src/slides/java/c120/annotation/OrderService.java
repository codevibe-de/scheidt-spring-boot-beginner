// Slide "@Transactional" (120)
package c120.annotation;

import c120.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class OrderService {

    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderRepository orderRepository;

    public OrderService(CustomerService customerService, ProductService productService,
                        OrderRepository orderRepository) {
        this.customerService = customerService;
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order placeOrder(String phoneNumber,
            Map<String, Integer> productQuantities) {
        Customer customer = customerService
                .getCustomerByPhoneNumber(phoneNumber);
        BigDecimal totalPrice = productService
                .getTotalPrice(productQuantities);
        Order order = new Order(customer, totalPrice);
        return orderRepository.save(order);
    }
}
