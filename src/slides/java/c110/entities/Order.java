// Slide "Entities annotieren" (110)
package c110.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @Column(name = "eta")
    private LocalDateTime estimatedTimeOfDelivery;

    protected Order() {
    }
}
