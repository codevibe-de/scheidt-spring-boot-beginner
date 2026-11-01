// Slide "REQUIRES_NEW: Transaktion anhalten" (120)
package c120.requiresnew;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
public class CustomerService {

    @Transactional(propagation = REQUIRES_NEW)
    public void increaseOrderCount(long id) {
    }
}
