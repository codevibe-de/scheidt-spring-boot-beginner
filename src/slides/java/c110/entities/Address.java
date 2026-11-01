// Slide "Eingebettete Objekte" (110)
package c110.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String street;
    private String postalCode;
    private String city;
    // ...
}
