// Slide "Validierung außerhalb des Controllers" (140)
package c140.validation;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class GreetingService {

    public String greet(@NotBlank String name) {
        return "Hallo " + name;
    }
}

// greet(" ") throws
// ConstraintViolationException: greet.name: darf nicht leer sein
