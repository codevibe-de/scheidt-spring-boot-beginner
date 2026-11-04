package pizza;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.ColumnData;
import com.github.freva.asciitable.HorizontalAlign;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pizza.customer.Customer;
import pizza.customer.CustomerService;
import pizza.order.OrderService;
import pizza.product.Product;
import pizza.product.ProductService;
import pizza.util.CommandLineRunner;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.github.freva.asciitable.HorizontalAlign.LEFT;
import static com.github.freva.asciitable.HorizontalAlign.RIGHT;

@Component
@Order(1)
public class LogicRunner implements CommandLineRunner {

    private final ProductService productService;
    private final CustomerService customerService;
    private final OrderService orderService;

    public LogicRunner(
            ProductService productService,
            CustomerService customerService,
            OrderService orderService
    ) {
        this.productService = productService;
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @Override
    public void run(String... args) {
        // Work with the data:
        var products = productService.getAllProducts();
        printTable("Products", products, List.of(
                column("ID", RIGHT, Product::getId),
                column("Name", LEFT, Product::getName),
                column("Price", RIGHT, p -> String.format("%.2f EUR", p.getPrice()))
        ));

        var customers = customerService.getAllCustomers();
        printTable("Customers", customers, List.of(
                column("ID", RIGHT, c -> String.valueOf(c.getId())),
                column("Name", LEFT, Customer::getFullName),
                column("Phone", LEFT, Customer::getPhoneNumber),
                column("Address", LEFT, c ->
                        c.getAddress().getStreet() + ", " + c.getAddress().getPostalCode() + " " + c.getAddress().getCity())
        ));

        if (!products.isEmpty() && !customers.isEmpty()) {
            var order = orderService.placeOrder(
                    customers.get(0).getPhoneNumber(),
                    Map.of(products.get(0).getId(), 2,
                            products.get(products.size() > 1 ? 1 : 0).getId(), 1)
            );
            printTable("Order placed", List.of(order), List.of(
                    column("ID", RIGHT, o -> String.valueOf(o.getId())),
                    column("Customer", LEFT, o -> o.getCustomer().getFullName()),
                    column("Total", RIGHT, o -> String.format("%.2f EUR", o.getTotalPrice())),
                    column("Est. Delivery", LEFT, o -> o.getEstimatedTimeOfDelivery().toLocalTime().toString())
            ));
        }
    }

    private static <T> void printTable(String title, Collection<T> data, List<ColumnData<T>> columns) {
        System.out.println("\n" + title + ":");
        System.out.println(AsciiTable.getTable(data, columns));
    }

    private static <T> ColumnData<T> column(String header, HorizontalAlign align, Function<T, String> extractor) {
        return new Column().header(header).headerAlign(align).dataAlign(align).with(extractor);
    }

}

