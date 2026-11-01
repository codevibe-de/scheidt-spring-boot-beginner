package pizza.customer;

/**
 * Projection for a phone list: only name and phone number of a {@link Customer}.
 * The components are named like the fields of the entity; that is how Spring Data maps them.
 */
public record CustomerContact(String fullName, String phoneNumber) {
}
