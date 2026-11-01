// Slide "Abgeleitete Abfragen" (110)
package c110.derived;

import c110.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerPhoneNumber(String phoneNumber);
}
