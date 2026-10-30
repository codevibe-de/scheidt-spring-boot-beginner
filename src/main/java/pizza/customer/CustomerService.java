package pizza.customer;

import org.springframework.stereotype.Service;

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

    public void increaseOrderCount(long customerId) {
        Customer customer = this.customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("For id `" + customerId + "`"));
        customer.increaseOrderCount();
    }

}
