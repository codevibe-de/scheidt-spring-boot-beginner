package pizza.product;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * Bonus d): the HTTP mapping of the product API as an interface. {@link ProductRestController}
 * implements it on the server side; a client can create a proxy from it with
 * {@code HttpServiceProxyFactory} (see {@code ProductApiTest}).
 */
@HttpExchange("/products")
public interface ProductApi {

    @GetExchange("/{id}")
    Product getProduct(@PathVariable("id") String productId);

    @GetExchange
    List<Product> getProducts();
}
