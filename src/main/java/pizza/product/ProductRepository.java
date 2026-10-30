package pizza.product;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data erzeugt die Implementierung zur Laufzeit; {@code save}, {@code existsById},
 * {@code findAll} und {@code findById} erbt das Interface von {@link JpaRepository}.
 */
public interface ProductRepository extends JpaRepository<Product, String> {
}
