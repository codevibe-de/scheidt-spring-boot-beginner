// Slide "Fallstrick 5: Isolation" (120)
package c120.isolation;

import c120.model.Customer;
import c120.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final CustomerService customerService;

    public OrderService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Transactional
    public Order placeOrderForNewCustomer(
            Customer newCustomer) {
        Customer customer =
                customerService.createCustomer(newCustomer);
        customerService.increaseOrderCount(customer.getId());
        // ...
        return null;
    }
}
