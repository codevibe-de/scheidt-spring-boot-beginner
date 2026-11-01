// Not on a slide: the entity behind ProductRepository on slide "Repositories" (110)
package c110.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "pk")
    private String id;

    private String name;

    private BigDecimal price;

    protected Product() {
    }
}
