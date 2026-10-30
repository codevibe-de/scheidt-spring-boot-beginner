# Übungen zum Kapitel "115 - DB-Migration mit Flyway"

Bisher legt Spring Boot das Datenbankschema bei jedem Start aus `schema.sql` neu an. Das geht nur,
solange die Datenbank im Speicher liegt und mit der Anwendung verschwindet. Eine echte Datenbank
behält ihre Daten, und ihr Schema muss mit jeder neuen Version der Anwendung mitwachsen. Diese
Änderungen beschreiben Sie in dieser Übung als Flyway-Migrationen.

Die Tests unter `src/test/java` laufen auf diesem Branch grün. Führen Sie sie nach jedem Schritt
aus: Sie müssen grün bleiben.

## a) Flyway einbinden

Ergänzen Sie in der `pom.xml` (bzw. `build.gradle`) den Starter `spring-boot-starter-flyway`.
Seit Spring Boot 4 reicht die Bibliothek `flyway-core` allein nicht mehr: Die Autoconfiguration
für Flyway steckt im Modul `spring-boot-flyway`, das der Starter mitbringt.

Legen Sie das Verzeichnis `src/main/resources/db/migration` an und verschieben Sie `schema.sql`
dorthin als `V1__create_tables.sql`. Entfernen Sie dabei die `DROP TABLE`-Anweisungen. Warum
gehören sie nicht in eine Migration?

Achten Sie auf den Dateinamen: großes `V`, Versionsnummer, **zwei** Unterstriche, Beschreibung.

Bauen Sie danach einmal komplett neu (`./mvnw clean` bzw. `./gradlew clean`). Sonst liegt die alte
`schema.sql` noch unter `target/classes`, und Boot führt sie nach Flyway aus.

Führen Sie die Tests aus und suchen Sie im Log die Zeilen von Flyway. Welche Migration wurde auf
welches Schema angewendet?

## b) Persistente Datenbank

Starten Sie den H2-Server mit `start-h2-server.sh` (bzw. `start-h2-server.bat`). Er speichert die
Datenbank unter `h2-data/` und behält sie über Neustarts hinweg.

Legen Sie für den Betrieb gegen diesen Server ein Profil `h2server` an, also eine Datei
`application-h2server.properties`, mit

````properties
spring.datasource.url=jdbc:h2:tcp://localhost:9092/./pizzadb
spring.datasource.username=sa
spring.datasource.password=
app.data-loader=none
````

Warum wäre der DataLoader `sample` hier keine gute Wahl? Denken Sie an den zweiten Start.

Starten Sie `PizzaApp` mit dem Profil `h2server` und öffnen Sie die H2-Konsole unter
http://localhost:8082 (JDBC-URL wie oben, Benutzer `sa`, kein Passwort). Was steht in der Tabelle
`flyway_schema_history`?
Flyway legt die Tabelle kleingeschrieben an, in H2 braucht ihr Name deshalb Anführungszeichen:
`SELECT * FROM "flyway_schema_history"`.

Starten Sie die Anwendung ein zweites Mal. Was meldet Flyway jetzt?

Ändern Sie zum Schluss in `V1__create_tables.sql` eine Kleinigkeit, etwa die Länge einer Spalte,
und starten Sie erneut. Lesen Sie die Fehlermeldung und nehmen Sie die Änderung wieder zurück.

## c) Das Schema ändern

Kunden sollen künftig eine E-Mail-Adresse haben können. Sie ist optional.

Ergänzen Sie in `Customer` ein Feld `email` mit Getter und Setter und starten Sie die Anwendung
mit dem Profil `h2server`. Welcher Teil der Anwendung beschwert sich, und warum?

Schreiben Sie die passende Migration `V2__add_customer_email.sql`, die die Spalte `email` als
`VARCHAR(100)` in `customers` ergänzt. Starten Sie erneut und sehen Sie in
`flyway_schema_history` nach. Laufen die Tests weiter?

## d) Bonus: Beispieldaten als wiederholbare Migration

Gegen den H2-Server startet die Anwendung jetzt ohne Produkte und Kunden. Die Beispieldaten sollen
dort wieder vorhanden sein, aber nur dort: Die Tests bringen ihre Daten über den DataLoader mit.

Legen Sie dafür ein zweites Verzeichnis `src/main/resources/db/sample-data` an und darin eine
wiederholbare Migration `R__sample_data.sql`. Sie soll die sechs Produkte und zwei Kunden des
DataLoader `sample` anlegen, und zwar so, dass sie beliebig oft laufen kann. In H2 geht das mit

````sql
MERGE INTO products (pk, name, price) KEY (pk) VALUES ('P-10', 'Pizza Margarita', 5.50);
````

Nehmen Sie das Verzeichnis nur im Profil `h2server` in die Liste der Migrationen auf, über die
Property `spring.flyway.locations`. Starten Sie zweimal. Wann läuft eine wiederholbare Migration
erneut? Probieren Sie es aus, indem Sie einen Preis ändern.
