package pizza.customer;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Address {

    //
    // --- fields ---
    //

    private String street;

    private String postalCode;

    private String city;

    //
    // --- constructors ---
    //

    /**
     * Für JPA, das Entities per Reflection erzeugt und die Felder danach befüllt.
     */
    protected Address() {
    }

    public Address(String street, String postalCode, String city) {
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
    }

    //
    // --- get / set ---
    //

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    //
    // --- other methods ---
    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street)
                && Objects.equals(postalCode, address.postalCode)
                && Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, postalCode, city);
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
