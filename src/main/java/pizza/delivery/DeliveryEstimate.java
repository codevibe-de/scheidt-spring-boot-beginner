package pizza.delivery;

/**
 * Antwort des Lieferdienstes, etwa {@code {"postalCode":"40302","minutes":27}}.
 */
public record DeliveryEstimate(
        String postalCode,
        int minutes
) {
}
