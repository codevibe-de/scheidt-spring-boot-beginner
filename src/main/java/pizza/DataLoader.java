package pizza;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pizza.customer.Address;
import pizza.customer.Customer;
import pizza.customer.CustomerService;
import pizza.product.Product;
import pizza.product.ProductService;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

/**
 * The <code>DataLoader</code> is an abstract class implementing the {@link Runnable}
 * interface, which adds basic helper methods for adding products and customers.
 * <p>
 * Some concrete extensions of this abstract class are implemented as static inner classes.
 */
public abstract class DataLoader implements Runnable {

    private final ProductService productService;
    private final CustomerService customerService;

    public DataLoader(ProductService productService, CustomerService customerService) {
        this.productService = productService;
        this.customerService = customerService;
    }

    protected void createProduct(String id, String name, String price) {
        this.productService.createProduct(new Product(id, name, new BigDecimal(price)));
    }

    protected Address createAddress(String street, String postalCode, String city) {
        return new Address(street, postalCode, city);
    }

    protected void createCustomer(String fullName, String phoneNumber, Address address) {
        this.customerService.createCustomer(new Customer(
                fullName,
                address,
                phoneNumber
        ));
    }

    //
    // --- concrete implementations ---
    //

    @Component("none")
    public static class None extends DataLoader {
        public None() {
            super(null, null);
        }

        @Override
        public void run() {
        }
    }


    @Component("sample")
    public static class Sample extends DataLoader {
        public Sample(ProductService productService, CustomerService customerService) {
            super(productService, customerService);
        }

        @Override
        public void run() {
            createProduct("S-01", "Tuna Salad", "6.90");
            createProduct("S-02", "Italian Salad", "7.90");
            createProduct("S-03", "Romaine Salad", "8.90");
            createProduct("P-10", "Pizza Margarita", "5.50");
            createProduct("P-11", "Pizza Capricciosa", "7.50");
            createProduct("P-12", "Pizza Spinach and Feta", "7.00");

            var address1 = createAddress("Wasserstr. 123", "40302", "Atlantis");
            var address2 = createAddress("Schlossallee 1", "88776", "Monopolhausen");

            createCustomer("Enrico Pallazzo", "+49 123 456789", address1);
            createCustomer("Elizabeth Magie", "+1 77 551237", address2);
        }
    }


    @Component("csv")
    public static class Csv extends DataLoader {

        @Value("${app.data-loader.csv.product-data}")
        Resource resource;

        public Csv(
                ProductService productService,
                CustomerService customerService
        ) {
            super(productService, customerService);
        }

        @Override
        public void run() {
            try {
                resource.getContentAsString(StandardCharsets.UTF_8).lines()
                        .filter(line -> !line.isBlank())
                        .forEach(this::parseAndCreateProduct);
            } catch (Throwable t) {
                throw new RuntimeException("Failed to load products from CSV resource " + resource, t);
            }
        }

        private void parseAndCreateProduct(String line) {
            String[] parts = line.split(";");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid product line: " + line);
            }
            createProduct(parts[0].trim(), parts[1].trim(), parts[2].trim());
        }
    }

}
