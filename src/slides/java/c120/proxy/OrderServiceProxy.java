// Slide "Was im Hintergrund passiert" (120)
package c120.proxy;

import c120.annotation.OrderService;
import c120.model.Order;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.Map;

// roughly how the proxy around OrderService works
public class OrderServiceProxy {

    private final PlatformTransactionManager txManager;
    private final TransactionDefinition definition = TransactionDefinition.withDefaults();
    private final OrderService target;

    public OrderServiceProxy(PlatformTransactionManager txManager, OrderService target) {
        this.txManager = txManager;
        this.target = target;
    }

    public Order placeOrder(String phone, Map<String, Integer> q) {
        TransactionStatus trx = txManager.getTransaction(definition);
        Order order;
        try {
            order = target.placeOrder(phone, q);
        } catch (RuntimeException | Error e) {
            txManager.rollback(trx);
            throw e;
        } catch (Exception e) {
            txManager.commit(trx);   // checked: Commit!
            throw e;
        }
        txManager.commit(trx);
        return order;
    }
}
