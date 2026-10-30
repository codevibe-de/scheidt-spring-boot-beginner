package pizza.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import pizza.customer.Customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    //
    // --- fields ---
    //

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private BigDecimal totalPrice;

    @Column(name = "eta")
    private LocalDateTime estimatedTimeOfDelivery;

    //
    // --- constructors ---
    //

    /**
     * Für JPA, das Entities per Reflection erzeugt und die Felder danach befüllt.
     */
    protected Order() {
    }

    public Order(Customer customer, BigDecimal totalPrice, LocalDateTime estimatedTimeOfDelivery) {
        this.customer = customer;
        this.totalPrice = totalPrice;
        this.estimatedTimeOfDelivery = estimatedTimeOfDelivery;
    }

    //
    // --- get / set ---
    //

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getEstimatedTimeOfDelivery() {
        return estimatedTimeOfDelivery;
    }

    //
    // --- other methods ---
    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", totalPrice=" + totalPrice +
                ", estimatedTimeOfDelivery=" + estimatedTimeOfDelivery +
                '}';
    }
}
