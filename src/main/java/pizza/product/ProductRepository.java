package pizza.product;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data creates the implementation at runtime; {@code save}, {@code existsById},
 * {@code findAll} and {@code findById} are inherited from {@link JpaRepository}.
 */
public interface ProductRepository extends JpaRepository<Product, String> {
}
