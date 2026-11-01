// Slide "Eigene Queries" (110)
package c110.queries;

import c110.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
            SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o
            WHERE o.customer.phoneNumber = :phoneNumber""")
    BigDecimal sumTotalPriceByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT * FROM orders WHERE total_price > :minimum", nativeQuery = true)
    List<Order> findExpensiveOrders(BigDecimal minimum);
}
