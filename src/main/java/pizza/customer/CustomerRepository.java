package pizza.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // abgeleitete Abfrage: Spring Data erzeugt die Query aus dem Methodennamen
    Optional<Customer> findByPhoneNumber(String phoneNumber);

    // Bonus f): Projection auf einen Record, Hibernate liest nur die beiden Spalten
    List<CustomerContact> findAllByOrderByFullName();
}
