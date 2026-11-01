// Slide "Paging und Sortierung" (110), the call below the repository
package c110.paging;

import c110.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findNewestOrders(String phoneNumber) {
        var newestFirst = Sort.by("estimatedTimeOfDelivery")
                .descending();
        Page<Order> page = orderRepository.findByCustomerPhoneNumber(
                phoneNumber, PageRequest.of(0, 10, newestFirst));

        page.getContent();        // the orders on this page
        page.getTotalElements();  // all matches
        return page.getContent();
    }
}
