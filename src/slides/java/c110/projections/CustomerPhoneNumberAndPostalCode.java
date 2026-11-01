// Slide "Projections" (110)
package c110.projections;

public interface CustomerPhoneNumberAndPostalCode {
    String getPhoneNumber();
    AddressPostalCode getAddress();

    interface AddressPostalCode {
        String getPostalCode();
    }
}
