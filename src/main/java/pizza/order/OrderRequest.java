package pizza.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Map;

public class OrderRequest {

    @NotBlank
    public String phoneNumber;

    // Übung b): Constraints an den Werten der Map gelten für jede einzelne Menge
    @NotEmpty
    public Map<String, @NotNull @Positive Integer> itemQuantities;

    @Override
    public String toString() {
        return "IncomingOrderDto{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", itemQuantities=" + itemQuantities +
                '}';
    }
}
