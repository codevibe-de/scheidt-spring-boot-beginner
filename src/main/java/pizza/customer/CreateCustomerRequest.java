package pizza.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// Übung b): Die Constraints prüft Spring erst, wenn der Controller den Parameter mit @Valid markiert.
public record CreateCustomerRequest(
        @NotBlank
        String fullName,
        Address address,
        @NotBlank
        @Pattern(regexp = "\\+?[0-9 ]+", message = "nur Ziffern, Leerzeichen und ein führendes +")
        String phoneNumber,
        @Email
        String email
) {
}
