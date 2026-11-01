package pizza.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// exercise c): the API only returns what the caller needs, not the entity with its customer.
public record OrderResponse(
        Long id,
        String phoneNumber,
        BigDecimal totalPrice,
        LocalDateTime estimatedTimeOfDelivery
) {

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomer().getPhoneNumber(),
                order.getTotalPrice(),
                order.getEstimatedTimeOfDelivery());
    }
}
