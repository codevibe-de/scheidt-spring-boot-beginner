package pizza.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // bonus e): derived query across the association Order.customer.phoneNumber
    List<Order> findByCustomerPhoneNumber(String phoneNumber);

    // bonus e): JPQL works with entity and field names, not with tables and columns
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.customer.phoneNumber = :phoneNumber")
    BigDecimal sumTotalPriceByPhoneNumber(String phoneNumber);
}
