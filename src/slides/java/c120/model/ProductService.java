// Not on a slide: the product service that the order services of chapter 120 call
package c120.model;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ProductService {

    public BigDecimal getTotalPrice(Map<String, Integer> productQuantities) {
        return BigDecimal.valueOf(5.5).multiply(BigDecimal.valueOf(
                productQuantities.values().stream().mapToInt(Integer::intValue).sum()));
    }
}
