// Slide "Repositories" (110)
package c110.repositories;

import c110.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
