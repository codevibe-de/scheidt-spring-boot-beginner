// Slide "Converter und ConversionService" (140)
package c140.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pizza.order.Order;
import pizza.order.OrderResponse;

@Component
public class OrderResponseConverter
        implements Converter<Order, OrderResponse> {

    @Override
    public OrderResponse convert(Order order) {
        return new OrderResponse(order.getId(), order.getCustomer().getPhoneNumber(),
                order.getTotalPrice(), order.getEstimatedTimeOfDelivery());
    }
}
