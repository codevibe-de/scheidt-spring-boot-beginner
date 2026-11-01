// Not on a slide: the order entity behind the services of chapter 120
package c120.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private BigDecimal totalPrice;

    protected Order() {
    }

    public Order(Customer customer, BigDecimal totalPrice) {
        this.customer = customer;
        this.totalPrice = totalPrice;
    }
}
