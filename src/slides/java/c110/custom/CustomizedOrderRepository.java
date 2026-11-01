// Slide "Customized Repositories" (110), the fragment interface is not shown on the slide
package c110.custom;

import c110.entities.Order;

import java.util.List;

public interface CustomizedOrderRepository {
    List<Order> findOverdueOrders();
}
