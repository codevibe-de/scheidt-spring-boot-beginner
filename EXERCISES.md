# Übungen zum Kapitel "150 - REST-Clients"

Die Pizza-App rechnet heute für jede Bestellung mit derselben Lieferzeit, dem Wert von
`app.order.delivery-time-in-minutes`. In dieser Übung fragt sie stattdessen einen externen
Analysedienst, den Lieferzeit-Dienst, der die Lieferzeit aus der Postleitzahl des Kunden schätzt.

Der Lieferzeit-Dienst liegt als einzelne Java-Datei im Projekt und braucht keinen Build. Starten Sie
ihn in einem eigenen Terminal im Projektverzeichnis:

````shell
java src/services/java/DeliveryTimeService.java
````

Er läuft auf Port 8081 und beantwortet `GET /estimates?postalCode=40302` mit
`{"postalCode":"40302","minutes":27}`. Er hat nur Daten für die Gebiete 40, 88, 97 und 99 (die ersten
beiden Ziffern der Postleitzahl), für alle anderen antwortet er mit 404. Das Gebiet 99 ist
überlastet: Dort kommt die Antwort erst nach 5 Sekunden.

Probieren Sie jeden Schritt mit den Anfragen in `requests.http` aus, die Anfragen zu diesem Kapitel
stehen unten in der Datei. Die Tests unter `src/test/java` müssen nach jedem Schritt grün bleiben.

Vorbereitet sind die Klasse `pizza.delivery.DeliveryTimeClient`, die schon in den `OrderService`
injiziert wird, die Methode `getDeliveryTimeInMinutes()` im `OrderService` und die Property
`app.delivery-time.base-url`.

## a) Die Lieferzeit beim Lieferzeit-Dienst erfragen

* Ergänzen Sie in der `pom.xml` (bzw. `build.gradle`) den Starter `spring-boot-starter-restclient`.
* Legen Sie ein Record `pizza.delivery.DeliveryEstimate` mit den Feldern `postalCode` und `minutes`
  an.
* Lassen Sie sich im Konstruktor von `DeliveryTimeClient` einen `RestClient.Builder` und die Property
  `app.delivery-time.base-url` injizieren. Bauen Sie daraus einen `RestClient` mit dieser Basis-URL.
* Implementieren Sie `getDeliveryTimeInMinutes()`: `GET /estimates?postalCode=...` aufrufen, die
  Antwort als `DeliveryEstimate` lesen und `minutes` zurückgeben.

````java
DeliveryEstimate estimate = restClient.get()
        .uri("/estimates?postalCode={postalCode}", postalCode)
        .retrieve()
        .body(DeliveryEstimate.class);
````

* Rufen Sie den `DeliveryTimeClient` in `OrderService#getDeliveryTimeInMinutes()` mit der Postleitzahl
  aus der Adresse des Kunden auf. Kunden ohne Adresse bekommen weiter den Wert aus der Property.

Bestellen Sie für Enrico Pallazzo (Postleitzahl 40302). Was steht in `estimatedTimeOfDelivery`, und
was zeigt das Terminal des Lieferzeit-Dienstes?

## b) Wenn der Lieferzeit-Dienst nicht antwortet

Probieren Sie drei Fälle aus und beobachten Sie, was der Aufrufer von `POST /orders` bekommt:

* der Kunde aus dem Gebiet 12 (`+49 30 12345` in `requests.http`, erst anlegen)
* der Lieferzeit-Dienst ist gestoppt
* der Kunde aus dem Gebiet 99 (`+49 361 99084`, erst anlegen)

Eine Bestellung soll nicht daran scheitern, dass der Lieferzeit-Dienst keine Lieferzeit liefert. Fangen
Sie im `OrderService` die `RestClientException` ab, schreiben Sie eine Warnung ins Log und nehmen Sie
den Wert aus `app.order.delivery-time-in-minutes`.

Gegen den langsamen Fall hilft das noch nicht. Setzen Sie in der `application.properties` einen
Read-Timeout für alle HTTP-Clients:

````properties
spring.http.clients.read-timeout=2s
````

Wie lange dauert die Bestellung im Gebiet 99 jetzt?

`placeOrder()` ist `@Transactional`, und der Aufruf des Lieferzeit-Dienstes passiert mitten in der
Transaktion. Was bedeutet das, wenn der Lieferzeit-Dienst langsam ist? Wo gehört der Aufruf besser hin?

## c) Bonus: derselbe Client als HTTP-Interface

Legen Sie ein Interface `pizza.delivery.DeliveryTimeApi` an:

````java
@HttpExchange("/estimates")
public interface DeliveryTimeApi {

    @GetExchange
    DeliveryEstimate getEstimate(@RequestParam String postalCode);
}
````

Spring Boot erzeugt die Implementierung, wenn Sie das Interface an `PizzaApp` importieren:

````java
@ImportHttpServices(group = "delivery-time", types = DeliveryTimeApi.class)
````

Die Basis-URL der Gruppe `delivery` setzen Sie in der `application.properties`:

````properties
spring.http.serviceclient.delivery-time.base-url=${app.delivery-time.base-url}
````

Lassen Sie `DeliveryTimeClient` statt des `RestClient` die `DeliveryTimeApi` benutzen. Gilt der
Read-Timeout aus b) auch hier?

## d) Bonus: ein Interface für Controller und Client

Ziehen Sie aus dem `ProductRestController` ein Interface `pizza.product.ProductApi` heraus, das die
HTTP-Abbildung mit `@HttpExchange("/products")` und `@GetExchange` beschreibt, und lassen Sie den
Controller es implementieren, ohne eigene `@GetMapping`s.

Schreiben Sie einen Test `ProductApiTest` mit `@SpringBootTest(webEnvironment = RANDOM_PORT)`, der
mit demselben Interface als Client gegen die laufende Anwendung ein Produkt abruft:

````java
RestClient restClient = RestClient.create("http://localhost:" + port);
ProductApi client = HttpServiceProxyFactory
        .builderFor(RestClientAdapter.create(restClient))
        .build()
        .createClient(ProductApi.class);
````

Für welche APIs eignet sich ein gemeinsames Interface, für welche nicht?
