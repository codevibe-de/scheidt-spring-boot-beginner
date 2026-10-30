package pizza.customer;

/**
 * Projection für eine Telefonliste: nur Name und Telefonnummer eines {@link Customer}.
 * Die Komponenten heißen wie die Felder der Entity, daran erkennt Spring Data die Zuordnung.
 */
public record CustomerContact(String fullName, String phoneNumber) {
}
