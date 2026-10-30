package pizza.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Bonus e): abgeleitete Abfrage über die Assoziation Order.customer.phoneNumber
    List<Order> findByCustomerPhoneNumber(String phoneNumber);

    // Bonus e): JPQL arbeitet mit Entity- und Feldnamen, nicht mit Tabellen und Spalten
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.customer.phoneNumber = :phoneNumber")
    BigDecimal sumTotalPriceByPhoneNumber(String phoneNumber);
}
