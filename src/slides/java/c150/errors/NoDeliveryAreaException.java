// Not on a slide: the exception thrown on slide "Fehler beim Aufruf" (150)
package c150.errors;

public class NoDeliveryAreaException extends RuntimeException {

    public NoDeliveryAreaException(String postalCode) {
        super("No delivery to postal code " + postalCode);
    }
}
