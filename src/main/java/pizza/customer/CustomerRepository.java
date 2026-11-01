package pizza.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // derived query: Spring Data creates the query from the method name
    Optional<Customer> findByPhoneNumber(String phoneNumber);

    // bonus f): projection onto a record, Hibernate reads only the two columns
    List<CustomerContact> findAllByOrderByFullName();
}
