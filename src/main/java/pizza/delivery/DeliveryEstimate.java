package pizza.delivery;

/**
 * Answer of the delivery time service, e.g. {@code {"postalCode":"40302","minutes":27}}.
 */
public record DeliveryEstimate(
        String postalCode,
        int minutes
) {
}
