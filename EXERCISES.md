# Exercises for chapter "150 - REST Clients"

Currently the pizza app calculates every order with the same delivery time, the value of
`app.order.delivery-time-in-minutes`. In this exercise it asks an external analysis service
instead, the delivery time service, which estimates the delivery time from the customer's postal
code.

The delivery time service is a single Java file in the project and needs no build. Start it in a
separate terminal in the project directory:

````shell
java src/services/java/DeliveryTimeService.java
````

It runs on port 8081 and answers `GET /estimates?postalCode=40302` with
`{"postalCode":"40302","minutes":27}`. It only has data for the areas 40, 88, 97 and 99 (the first
two digits of the postal code); for all others it answers with 404. Area 99 is overloaded: the
answer only comes after 5 seconds.

Try out every step with the requests in `requests.http`; the requests for this chapter are at the
bottom of the file. The tests under `src/test/java` must stay green after every step.

Prepared are the class `pizza.delivery.DeliveryTimeClient`, which is already injected into the
`OrderService`, the method `getDeliveryTimeInMinutes()` in the `OrderService` and the property
`app.delivery-time.base-url`.

## a) Asking the delivery time service for the delivery time

* Add the starter `spring-boot-starter-restclient` to the `pom.xml`.
* Create a record `pizza.delivery.DeliveryEstimate` with the fields `postalCode` and `minutes`.
* Have a `RestClient.Builder` and the property `app.delivery-time.base-url` injected into the
  constructor of `DeliveryTimeClient`. Use them to build a `RestClient` with this base URL.
* Implement `getDeliveryTimeInMinutes()`: call `GET /estimates?postalCode=...`, read the response
  as `DeliveryEstimate` and return `minutes`.

````java
DeliveryEstimate estimate = restClient.get()
        .uri("/estimates?postalCode={postalCode}", postalCode)
        .retrieve()
        .body(DeliveryEstimate.class);
````

* Call the `DeliveryTimeClient` in `OrderService#getDeliveryTimeInMinutes()` with the postal code
  from the customer's address. Customers without an address still get the value from the property.

Place an order for Enrico Pallazzo (postal code 40302). What is in `estimatedTimeOfDelivery`, and
what does the terminal of the delivery time service show?

## b) When the delivery time service does not answer

Try out three cases and watch what the caller of `POST /orders` gets:

* the customer from area 12 (`+49 30 12345` in `requests.http`, create it first)
* the delivery time service is stopped
* the customer from area 99 (`+49 361 99084`, create it first)

An order should not fail because the delivery time service does not deliver a delivery time. Catch
the `RestClientException` in the `OrderService`, write a warning to the log and take the value from
`app.order.delivery-time-in-minutes`.

That does not help with the slow case yet. Set a read timeout for all HTTP clients in
`application.properties`:

````properties
spring.http.clients.read-timeout=2s
````

How long does the order in area 99 take now?

`placeOrder()` is `@Transactional`, and the call to the delivery time service happens in the middle
of the transaction. What does that mean when the delivery time service is slow? Where would the
call be better placed?

## c) Bonus: the same client as an HTTP interface

Create an interface `pizza.delivery.DeliveryTimeApi`:

````java
@HttpExchange("/estimates")
public interface DeliveryTimeApi {

    @GetExchange
    DeliveryEstimate getEstimate(@RequestParam String postalCode);
}
````

Spring Boot creates the implementation when you import the interface on `PizzaApp`:

````java
@ImportHttpServices(group = "delivery-time", types = DeliveryTimeApi.class)
````

Set the base URL of the group `delivery-time` in `application.properties`:

````properties
spring.http.serviceclient.delivery-time.base-url=${app.delivery-time.base-url}
````

Let `DeliveryTimeClient` use the `DeliveryTimeApi` instead of the `RestClient`. Does the read
timeout from b) apply here too?

## d) Bonus: one interface for controller and client

Extract an interface `pizza.product.ProductApi` from the `ProductRestController` that describes the
HTTP mapping with `@HttpExchange("/products")` and `@GetExchange`, and let the controller implement
it, without `@GetMapping`s of its own.

Write a test `ProductApiTest` with `@SpringBootTest(webEnvironment = RANDOM_PORT)` that uses the
same interface as a client to fetch a product from the running application:

````java
RestClient restClient = RestClient.create("http://localhost:" + port);
ProductApi client = HttpServiceProxyFactory
        .builderFor(RestClientAdapter.create(restClient))
        .build()
        .createClient(ProductApi.class);
````

For which APIs is a shared interface a good fit, and for which not?
