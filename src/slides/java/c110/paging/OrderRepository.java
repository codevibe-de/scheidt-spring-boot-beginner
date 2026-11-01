// Slide "Paging und Sortierung" (110)
package c110.paging;

import c110.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    Page<Order> findByCustomerPhoneNumber(
            String phoneNumber, Pageable pageable);
}
