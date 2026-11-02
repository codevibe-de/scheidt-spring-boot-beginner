// Slide "Converter und ConversionService" (140), the part "injected into the controller"
package c140.conversion;

import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pizza.order.Order;
import pizza.order.OrderResponse;
import pizza.order.OrderService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OrderRestController {

    private final OrderService orderService;

    // injected into the controller
    private final ConversionService conversionService;

    public OrderRestController(OrderService orderService, ConversionService conversionService) {
        this.orderService = orderService;
        this.conversionService = conversionService;
    }

    @GetMapping("/orders")
    public List<OrderResponse> getOrders() {
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orderService.getOrders()) {
            OrderResponse response = conversionService.convert(order, OrderResponse.class);
            responses.add(response);
        }
        return responses;
    }
}
