package pizza.product;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * Bonus d): die HTTP-Abbildung der Produkt-API als Interface. Der {@link ProductRestController}
 * implementiert es auf der Server-Seite, ein Client kann daraus mit {@code HttpServiceProxyFactory}
 * einen Proxy erzeugen (siehe {@code ProductApiTest}).
 */
@HttpExchange("/products")
public interface ProductApi {

    @GetExchange("/{id}")
    Product getProduct(@PathVariable("id") String productId);

    @GetExchange
    List<Product> getProducts();
}
