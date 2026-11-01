package pizza.product;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// bonus d): the URL mapping lives in ProductApi, only the implementation is left here
@RestController
public class ProductRestController implements ProductApi {

    //
    // --- injected beans ---
    //

    private final ProductService productService;

    //
    // --- constructors and setup ---
    //

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    //
    // --- REST endpoints ---
    //

    @Override
    public Product getProduct(String productId) {
        return this.productService.getProduct(productId);
    }

    @Override
    public List<Product> getProducts() {
        return this.productService.getAllProducts();
    }
}
