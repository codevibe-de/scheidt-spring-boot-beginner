// Slide "Fallstrick 3: readOnly" (120)
package c120.readonly;

import c120.model.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public void increaseOrderCount(long customerId) {
        customerRepository.findById(customerId)
                .orElseThrow()
                .increaseOrderCount();
        // no exception, but the counter stays 0
    }
}
