// Not on a slide: the customer service that the order services of chapter 120 call
package c120.model;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomerByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber).orElseThrow();
    }

    @Transactional
    public void increaseOrderCount(long customerId) {
        customerRepository.findById(customerId).orElseThrow().increaseOrderCount();
    }
}
