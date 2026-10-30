# Übungen zum Kapitel "130 - Testen mit Boot"

Bisher starten alle Tests unter `src/test/java` den kompletten Kontext mit `@SpringBootTest`. Das
ist gründlich, aber langsam, und die Tests hängen an den Beispieldaten des DataLoader. In dieser
Übung testen Sie den `OrderService` ohne Spring, mit Mocks im Kontext, und das `OrderRepository`
in einem Slice.

Die Tests laufen auf diesem Branch grün; die beiden Testklassen für a) und b) sind als Gerüst
vorbereitet, ihre Methoden noch leer.

## a) Unit-Test mit Mockito

Vervollständigen Sie `pizza.order.OrderServiceUnitTest`. Der Test kommt ganz ohne Spring aus: Er
erzeugt den `OrderService` selbst mit `new`, seine Abhängigkeiten sind Mocks.

* Lassen Sie die drei Mocks von Mockito erzeugen: `@ExtendWith(MockitoExtension.class)` an der
  Klasse, `@Mock` an den Feldern.
* Programmieren Sie die Mocks mit `Mockito.when(...).thenReturn(...)` bzw. `.thenThrow(...)`. Für
  `save()` gibt `.thenAnswer(invocation -> invocation.getArgument(0))` die übergebene Bestellung
  zurück.
* Prüfen Sie mit `Mockito.verify(...)`, dass der Zähler erhöht und die Bestellung gespeichert
  wurde, und im Fehlerfall mit `Mockito.verifyNoInteractions(...)`, dass das `OrderRepository`
  gar nicht benutzt wurde.

Warum setzt `setUp()` die Id des Kunden per `ReflectionTestUtils`? Was passiert ohne?

Was meldet Mockito, wenn Sie in `setUp()` etwas programmieren, das ein Test nicht braucht?

## b) Mocks und Spies im Kontext

Vervollständigen Sie `pizza.order.OrderServiceMockBeanTest`. Hier läuft der volle Kontext, aber
zwei Beans werden ausgetauscht:

* der `ProductService` durch einen Mock mit `@MockitoBean`: Er liefert den Preis, den der Test
  vorgibt
* der `CustomerService` durch einen Spy mit `@MockitoSpyBean`: Er ruft die echten Methoden auf,
  zeichnet die Aufrufe aber auf

Prüfen Sie im zweiten Test, dass `increaseOrderCount()` auch bei einer gescheiterten Bestellung
genau einmal aufgerufen wird (`Mockito.verify(customerService, Mockito.times(1))`) und dass der
Zähler in der Datenbank wirklich gestiegen ist. Macht der Spy die Transaktion aus Kapitel 120 mit?

Führen Sie danach alle Tests aus und zählen Sie im Log die Zeilen `Started ...Test in`. Wie viele
Kontexte startet Spring, und warum?

Tipp: Mit dem Logger `org.springframework.test.context.cache` auf `DEBUG` schreibt Spring nach
jeder Testklasse eine Statistik des Kontext-Caches (`size`, `hitCount`, `missCount`). Am
einfachsten setzen Sie ihn über eine Umgebungsvariable, die Maven und Gradle an die Tests
weiterreichen:

````shell
# macOS, Linux
LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_TEST_CONTEXT_CACHE=DEBUG ./mvnw test

# Windows PowerShell
$env:LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_TEST_CONTEXT_CACHE="DEBUG"; ./mvnw test
````

Mit Maven geht auch `./mvnw test -Dlogging.level.org.springframework.test.context.cache=DEBUG`;
Gradle reicht `-D` nicht an die Tests weiter. Gradle zeigt die Ausgabe der Tests außerdem nicht in
der Konsole, sie steht im Bericht `build/reports/tests/test/index.html`. In IntelliJ tragen Sie
die Variable in der Run Configuration unter *Environment variables* ein.

## c) Ein Slice für JPA

Schreiben Sie einen Test `pizza.order.OrderRepositoryTest` mit `@DataJpaTest` für die beiden
Abfragen `findByCustomerPhoneNumber()` und `sumTotalPriceByPhoneNumber()`.

Seit Spring Boot 4 liegt `@DataJpaTest` in einem eigenen Modul. Ergänzen Sie in der `pom.xml`
(bzw. `build.gradle`) den Starter `spring-boot-starter-data-jpa-test` mit Scope `test`.

Legen Sie die Testdaten in einer `@BeforeEach`-Methode mit dem `TestEntityManager` an, etwa so:

````java
customer = entityManager.persist(new Customer("Trans Action", null, "0931 123456"));
entityManager.persist(new Order(customer, new BigDecimal("12.50"), LocalDateTime.now()));
entityManager.persist(new Order(customer, new BigDecimal("7.40"), LocalDateTime.now()));
entityManager.flush();
entityManager.clear();
````

Wozu dienen `flush()` und `clear()`?

Beantworten Sie anhand des Logs und eines Tests mit `orderRepository.count()`:

* Gegen welche Datenbank läuft der Test, und wer legt dort das Schema an?
* Gibt es die Beispieldaten des DataLoader?
* Warum sieht ein Test die Daten des vorigen nicht?

## d) Bonus: Testcontainers

Diesen Teil können Sie nur bearbeiten, wenn auf Ihrem Rechner Docker läuft.

Schreiben Sie einen Test `pizza.PostgresIntegrationTest`, der die Anwendung gegen ein echtes
PostgreSQL in einem Container startet. Sie brauchen dafür mit Scope `test`:

* `org.springframework.boot:spring-boot-testcontainers`
* `org.testcontainers:testcontainers-junit-jupiter`
* `org.testcontainers:testcontainers-postgresql`
* `org.postgresql:postgresql`
* `org.flywaydb:flyway-database-postgresql`

Die Versionen verwaltet Spring Boot. Der Container steht als statisches Feld in der Testklasse:

````java
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
class PostgresIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");
````

Prüfen Sie, dass Flyway die drei Migrationen auf PostgreSQL anwendet und dass eine Bestellung
gespeichert wird. Woher kennt Spring die JDBC-URL der Datenbank im Container?
