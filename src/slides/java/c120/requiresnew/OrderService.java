// Slide "REQUIRES_NEW: Transaktion anhalten" (120)
package c120.requiresnew;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final CustomerService customerService;

    public OrderService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Transactional
    public void placeOrder(long id) {

        customerService
            .increaseOrderCount(id);


    }
}
