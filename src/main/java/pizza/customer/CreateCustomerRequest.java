package pizza.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// exercise b): Spring only checks the constraints once the controller marks the parameter with @Valid.
public record CreateCustomerRequest(
        @NotBlank
        String fullName,
        Address address,
        @NotBlank
        @Pattern(regexp = "\\+?[0-9 ]+", message = "only digits, spaces and a leading +")
        String phoneNumber,
        @Email
        String email
) {
}
