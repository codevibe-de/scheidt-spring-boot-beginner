# Exercises for chapter "140 - Advanced REST"

The REST API of the pizza app works as long as all requests are correct. In this exercise it gets
error handling, validates its input and only hands out what a caller needs for orders.

Start the application and try out every step with the requests in `requests.http`; the requests
for this chapter are at the bottom of the file. The tests under `src/test/java` must stay green
after every step.

## a) Error handling

`GET /products/X-99` currently returns HTTP 500, although the caller only gave an unknown number.

Create a class `pizza.web.ApiExceptionHandler` with `@RestControllerAdvice`. A method with
`@ExceptionHandler` should catch `ProductNotFoundException` and `CustomerNotFoundException` and
return a `ProblemDetail` with status 404:

````java
return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
````

Which `Content-Type` does the response have now?

`POST /orders` with an unknown phone number now returns 404 as well. Is that the right status? What
would you choose instead?

## b) Validating input

The API currently even creates a customer without a name and with the phone number "abc".

* Add the starter `spring-boot-starter-validation` to the `pom.xml`.
* Add a field `email` to `CreateCustomerRequest`, so that the email from chapter 115 can also be
  set via the API, and pass it on to the new customer in the `CustomerRestController` with
  `setEmail()`.
* Annotate the fields of `CreateCustomerRequest`: `fullName` must not be blank (`@NotBlank`),
  neither must `phoneNumber`, which should also consist only of digits, spaces and a leading `+`
  (`@Pattern`), and `email` must be a valid address if given (`@Email`).
* Annotate `OrderRequest`: the phone number must not be blank, there must be at least one item, and
  every quantity must be greater than 0. Constraints for the values of a `Map` go on the type
  parameter: `Map<String, @NotNull @Positive Integer>`.
* Mark the two `@RequestBody` parameters in the controllers with `@Valid`.

The invalid requests now return 400. Does the caller learn which field is wrong? Where does it say
so?

Set the property `spring.mvc.problemdetails.enabled=true` in `application.properties`. What
changes in the response?

To give the caller the field errors, let `ApiExceptionHandler` extend
`ResponseEntityExceptionHandler` and override `handleMethodArgumentNotValid()`. The field errors
are in `ex.getBindingResult().getFieldErrors()`; with `ex.getBody().setProperty("errors", ...)`
they go into the `ProblemDetail` as an additional field. Then call the superclass method.

The property from the previous step now has no effect. Why?

## c) A response DTO for orders

`POST /orders` and `GET /orders` return the `Order` entity, including the complete customer with
address, email and the internal counter `orderCount`.

Create a record `pizza.order.OrderResponse` with `id`, `phoneNumber`, `totalPrice` and
`estimatedTimeOfDelivery`, plus a static method `from(Order order)`. Let both methods of the
`OrderRestController` return `OrderResponse` instead of `Order`.

What else speaks against handing out entities directly as JSON?

## d) Bonus: a static start page

Create a file `index.html` under `src/main/resources/static` that shows the products as a table. It
fetches the data from the API via JavaScript:

````javascript
fetch('/products')
    .then(response => response.json())
    .then(products => { /* create table rows */ });
````

The stylesheet `bootstrap-5.3.3.css` is already in the same directory. At which URL is the page
available, and why?
