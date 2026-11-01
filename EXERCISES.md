# Übungen zum Kapitel "140 - REST fortgeschritten"

Die REST-API der Pizza-App funktioniert, solange alle Anfragen stimmen. In dieser Übung bekommt
sie eine Fehlerbehandlung, prüft ihre Eingaben und gibt bei Bestellungen nur noch heraus, was ein
Aufrufer braucht.

Starten Sie die Anwendung und probieren Sie jeden Schritt mit den Anfragen in `requests.http` aus,
die Anfragen zu diesem Kapitel stehen unten in der Datei. Die Tests unter `src/test/java` müssen
nach jedem Schritt grün bleiben.

## a) Fehlerbehandlung

`GET /products/X-99` liefert heute HTTP 500, obwohl der Aufrufer nur eine unbekannte Nummer
angegeben hat.

Legen Sie eine Klasse `pizza.web.ApiExceptionHandler` mit `@RestControllerAdvice` an. Eine Methode
mit `@ExceptionHandler` soll `ProductNotFoundException` und `CustomerNotFoundException` abfangen und
ein `ProblemDetail` mit Status 404 zurückgeben:

````java
return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
````

Welchen `Content-Type` hat die Antwort jetzt?

Auch `POST /orders` mit einer unbekannten Telefonnummer liefert nun 404. Ist das der richtige
Status? Was würden Sie stattdessen wählen?

## b) Eingaben validieren

Die API legt heute auch einen Kunden ohne Namen und mit der Telefonnummer "abc" an.

* Ergänzen Sie in der `pom.xml` (bzw. `build.gradle`) den Starter
  `spring-boot-starter-validation`.
* Ergänzen Sie `CreateCustomerRequest` um ein Feld `email`, damit die E-Mail aus Kapitel 115 auch
  über die API angelegt werden kann, und übernehmen Sie sie im `CustomerRestController` mit
  `setEmail()` in den neuen Kunden.
* Annotieren Sie die Felder von `CreateCustomerRequest`: `fullName` darf nicht leer sein
  (`@NotBlank`), `phoneNumber` ebenfalls und soll nur aus Ziffern, Leerzeichen und einem führenden
  `+` bestehen (`@Pattern`), `email` muss eine gültige Adresse sein, falls angegeben (`@Email`).
* Annotieren Sie `OrderRequest`: Die Telefonnummer darf nicht leer sein, es muss mindestens eine
  Position geben, und jede Menge muss größer als 0 sein. Constraints für die Werte einer `Map`
  stehen am Typparameter: `Map<String, @NotNull @Positive Integer>`.
* Markieren Sie die beiden `@RequestBody`-Parameter in den Controllern mit `@Valid`.

Die ungültigen Anfragen liefern jetzt 400. Erfährt der Aufrufer, welches Feld falsch ist? Wo steht
es?

Setzen Sie in der `application.properties` die Property `spring.mvc.problemdetails.enabled=true`.
Was ändert sich an der Antwort?

Damit der Aufrufer die Feldfehler bekommt, lassen Sie `ApiExceptionHandler` von
`ResponseEntityExceptionHandler` erben und überschreiben `handleMethodArgumentNotValid()`. Die
Feldfehler stehen in `ex.getBindingResult().getFieldErrors()`; mit
`ex.getBody().setProperty("errors", ...)` kommen sie als zusätzliches Feld in das `ProblemDetail`.
Rufen Sie danach die Methode der Oberklasse auf.

Die Property aus dem vorigen Schritt ist jetzt wirkungslos. Warum?

## c) Ein Response-DTO für Bestellungen

`POST /orders` und `GET /orders` geben die `Order`-Entity zurück, samt dem kompletten Kunden mit
Adresse, E-Mail und dem internen Zähler `orderCount`.

Legen Sie ein Record `pizza.order.OrderResponse` mit `id`, `phoneNumber`, `totalPrice` und
`estimatedTimeOfDelivery` an, dazu eine statische Methode `from(Order order)`. Lassen Sie beide
Methoden des `OrderRestController` `OrderResponse` statt `Order` zurückgeben.

Was spricht noch dagegen, Entities direkt als JSON herauszugeben?

## d) Bonus: eine statische Startseite

Legen Sie unter `src/main/resources/static` eine Datei `index.html` an, die die Produkte als Tabelle
zeigt. Die Daten holt sie per JavaScript von der API:

````javascript
fetch('/products')
    .then(response => response.json())
    .then(products => { /* Tabellenzeilen erzeugen */ });
````

Das Stylesheet `bootstrap-5.3.3.css` liegt schon im selben Verzeichnis. Unter welcher URL ist die
Seite erreichbar, und warum?
