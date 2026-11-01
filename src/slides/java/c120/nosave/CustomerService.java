// Slide "Fallstrick 1: Änderungen ohne save()" (120)
package c120.nosave;

import c120.model.Customer;
import c120.model.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void increaseOrderCount(long customerId) {
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow();
        customer.increaseOrderCount();
        // no save(): Hibernate writes the
        // change on commit
    }
}
