// Slide "Projections für Joins" (110); the slide names the app's class pizza.order.OrderRepository
package c110.projections;

import c110.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    record OrderSummary(Long orderId, String customerFullName) {
    }

    @Query("""
            SELECT new c110.projections.OrderRepository$OrderSummary(
                       o.id, c.fullName)
            FROM Order o JOIN o.customer c
            WHERE c.id = :customerId
            """)
    List<OrderSummary> findOrderSummariesByCustomerId(
            Long customerId);
}
