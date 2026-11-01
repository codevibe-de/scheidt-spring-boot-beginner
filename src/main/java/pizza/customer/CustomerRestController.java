package pizza.customer;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerRestController {

    //
    // --- constants ---
    //

    public static final String ROOT = "/customers";
    public static final String GET_ALL_PATH = ROOT;
    public static final String CREATE_PATH = ROOT;

    //
    // --- injected beans ---
    //

    private final CustomerService customerService;

    //
    // --- constructors and setup ---
    //

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //
    // --- REST endpoints ---
    //

    @GetMapping(GET_ALL_PATH)
    public Iterable<Customer> getAllCustomers() {
        return this.customerService.getAllCustomers();
    }

    @PostMapping(CREATE_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
        Customer customer = new Customer(
                createCustomerRequest.fullName(),
                createCustomerRequest.address(),
                createCustomerRequest.phoneNumber()
        );
        customer.setEmail(createCustomerRequest.email());
        return this.customerService.createCustomer(customer);
    }

}
