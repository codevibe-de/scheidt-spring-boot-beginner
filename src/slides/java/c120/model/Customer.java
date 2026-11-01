// Not on a slide: the customer entity behind the services of chapter 120
package c120.model;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String fullName;

    private String phoneNumber;

    private int orderCount;

    protected Customer() {
    }

    public Customer(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public void increaseOrderCount() {
        orderCount++;
    }

    public Long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getOrderCount() {
        return orderCount;
    }
}
