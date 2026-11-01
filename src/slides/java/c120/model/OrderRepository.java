// Not on a slide: repository used by the services of chapter 120
package c120.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
