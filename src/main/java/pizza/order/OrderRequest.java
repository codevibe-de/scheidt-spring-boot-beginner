package pizza.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Map;

public class OrderRequest {

    @NotBlank
    public String phoneNumber;

    // exercise b): constraints on the values of the map apply to every single quantity
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
