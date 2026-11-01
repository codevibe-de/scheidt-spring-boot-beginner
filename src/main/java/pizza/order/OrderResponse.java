package pizza.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Übung c): Die API gibt nur heraus, was der Aufrufer braucht, nicht die Entity samt Kunde.
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
