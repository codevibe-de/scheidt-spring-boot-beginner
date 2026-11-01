package pizza.customer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    //
    // constructors and setup
    //

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    //
    // business logic
    //

    public Customer getCustomerByPhoneNumber(String phoneNumber) {
        return this.customerRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomerNotFoundException("For phoneNumber `" + phoneNumber + "`"));
    }

    public List<Customer> getAllCustomers() {
        return this.customerRepository.findAll();
    }

    public Customer createCustomer(Customer customer) {
        return this.customerRepository.save(customer);
    }

    public List<CustomerContact> getCustomerContacts() {
        return this.customerRepository.findAllByOrderByFullName();
    }

    // exercise a): inside the transaction the customer is "managed", the change is written on commit without save().
    // exercise c): REQUIRES_NEW suspends the caller's transaction and commits the counter independently of it.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseOrderCount(long customerId) {
        Customer customer = this.customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("For id `" + customerId + "`"));
        customer.increaseOrderCount();
    }

}
