// Slide "Projections -- Hinweise" (110)
package c110.projections;

import c110.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<CustomerContact> findAllByOrderByFullName();
    // select c1_0.full_name,c1_0.phone_number from customers c1_0 order by c1_0.full_name

    List<CustomerPhoneNumberAndPostalCode> findAllProjectedBy();
}
