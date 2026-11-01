// Slide "Eingebettete Objekte" (110)
package c110.entities;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String fullName;
    @Embedded
    private Address address;
    private String phoneNumber;
    // ...
}
