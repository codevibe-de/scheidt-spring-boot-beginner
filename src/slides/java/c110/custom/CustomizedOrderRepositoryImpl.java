// Slide "Customized Repositories" (110)
package c110.custom;

import c110.entities.Order;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

public class CustomizedOrderRepositoryImpl
        implements CustomizedOrderRepository {

    private final EntityManager em;

    public CustomizedOrderRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Order> findOverdueOrders() {
        return em.createQuery("""
                        SELECT o FROM Order o
                        WHERE o.estimatedTimeOfDelivery < :now
                        """, Order.class)
                .setParameter("now", LocalDateTime.now())
                .getResultList();
    }
}
