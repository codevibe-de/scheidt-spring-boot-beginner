// Slide "Fallstrick 2: Aufruf in derselben Klasse" (120)
package c120.selfinvocation;

import c120.model.Customer;
import c120.model.CustomerService;
import c120.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final CustomerService customerService;

    public OrderService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Transactional
    public Order placeOrder(String phoneNumber) {
        Customer customer = customerService.getCustomerByPhoneNumber(phoneNumber);
        countOrderAttempt(customer.getId());
        // ...
        return null;
    }

    // no effect: call via this, not via the proxy
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void countOrderAttempt(long customerId) {
        customerService.increaseOrderCount(customerId);
    }
}
