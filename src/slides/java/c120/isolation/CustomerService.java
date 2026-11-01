// Slide "Fallstrick 5: Isolation" (120)
package c120.isolation;

import c120.model.Customer;
import c120.model.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer createCustomer(Customer newCustomer) {
        return customerRepository.save(newCustomer);
    }

    // in the CustomerService
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseOrderCount(long customerId) {
        customerRepository.findById(customerId)
                .orElseThrow()
                .increaseOrderCount();
    }
}
