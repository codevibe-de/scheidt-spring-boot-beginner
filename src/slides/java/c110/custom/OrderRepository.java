// Slide "Customized Repositories" (110)
package c110.custom;

import c110.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends
        JpaRepository<Order, Long>, CustomizedOrderRepository {
}
