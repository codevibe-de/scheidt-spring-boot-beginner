// Slide "Fallstrick 4: Rollback" (120)
package c120.rollback;

import c120.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Transactional(rollbackFor = Exception.class)
    public Order placeOrder(String phoneNumber) throws Exception {
        // ...
        return null;
    }
}
