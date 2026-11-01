package pizza.customer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Comparator;

// @SpringBootTest boots the context via SpringApplication (like the real app), so Boot's
// CommandLineRunners — including the DataLoadRunner — actually run. The "test" profile keeps
// the noisy LogicRunner out (see @Profile("!test") on it).
@SpringBootTest
@ActiveProfiles("test")
class CustomerServiceTest {

    @Autowired
    CustomerService customerService;

    @Test
    void createCustomer() {
        // given
        Address address = new Address("123 Main St", "12345", "Anytown");
        Customer customer = new Customer("John Doe", address, "0170-123123123");

        // when
        Customer createdCustomer = customerService.createCustomer(customer);

        // then
        Assertions.assertThat(createdCustomer).isNotNull();
        Assertions.assertThat(createdCustomer.getId()).isNotNull();
    }

    @Test
    void getCustomerByPhoneNumber() {
        // when -- customer from the sample data of the DataLoader "sample"
        Customer customer = customerService.getCustomerByPhoneNumber("+1 77 551237");

        // then -- the address must come back from the database completely as well
        Assertions.assertThat(customer.getFullName()).isEqualTo("Elizabeth Magie");
        Assertions.assertThat(customer.getAddress())
                .isEqualTo(new Address("Schlossallee 1", "88776", "Monopolhausen"));
    }

    // bonus f)
    @Test
    void getCustomerContacts() {
        Assertions.assertThat(customerService.getCustomerContacts())
                .contains(new CustomerContact("Elizabeth Magie", "+1 77 551237"))
                .isSortedAccordingTo(Comparator.comparing(CustomerContact::fullName));
    }
}
