package pizza;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.ColumnData;
import com.github.freva.asciitable.HorizontalAlign;
import pizza.customer.Customer;
import pizza.customer.CustomerService;
import pizza.order.OrderService;
import pizza.product.HashMapProductRepository;
import pizza.product.Product;
import pizza.product.ProductService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.github.freva.asciitable.HorizontalAlign.LEFT;
import static com.github.freva.asciitable.HorizontalAlign.RIGHT;

public class PizzaApp {

    public static void main(String[] args) {
        // Instantiate beans ---
        ProductService productService = new ProductService(new HashMapProductRepository());
        CustomerService customerService = new CustomerService();
        OrderService orderService = new OrderService(customerService, productService);
        new DataLoader.Sample(productService, customerService).run();

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
            System.out.println("\n=== Now placing a sample order… ===");
            var order = orderService.placeOrder(
                    customers.get(0).getPhoneNumber(),
                    Map.of(products.get(0).getId(), 2,
                            products.get(products.size() > 1 ? 1 : 0).getId(), 1)
            );
            printTable("Orders", List.of(order), List.of(
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
