# Exercises for chapter "080 - RESTful API"

## a) Clients

### Insomnia

If you like, you can install the "Insomnia" REST client: https://insomnia.rest/

From this branch on, the project contains an exported Insomnia workspace that you can import into the tool. It lets
you conveniently call the endpoints you will program later.

### IntelliJ

There is now also a file `requests.http` in the project root directory. You can open it in IntelliJ **Ultimate** and
run the requests directly from there with the green arrow.

### curl

The command-line tool `curl` can be used as follows:

- a simple GET on an address:
  ````shell
  curl http://localhost:8080/products
  ````
- a POST with a body whose content is read from a file:
  ````shell
  curl -d @<filename> http://localhost:8080/orders
  ````
- a DELETE with a query string:
  ````shell
  curl -X DELETE http://localhost:8080/products?namePrefix=Pizza
  ````

### Other clients

Of course you can also use other tools such as Postman to test the endpoints.

## b) OrderRestController

Create an API endpoint `/orders/greeting` that returns a greeting as a string for a GET.

Test the URL in your browser: http://localhost:8080/orders/greeting

## c) CustomerRestController

Create the following API endpoints:

* `GET /customers`, which returns all customers
* `POST /customers`, which creates a new customer. The customer is defined by the following JSON content:

````json
{
  "fullName": "Enrico Pallazzo",
  "address": {
    "street": "Wasserstr. 123",
    "postalCode": "40302",
    "city": "Atlantis"
  },
  "phoneNumber": "+49 123 456789"
}
````

`Customer` itself is not suitable as a `@RequestBody`: the class has two constructors and no default constructor, so
Jackson doesn't know how to create it. Instead, create a record `CreateCustomerRequest` with the three fields from the
JSON, accept it in the controller method and create the `Customer` for the `CustomerService` from it.

## d) ProductRestController

Create the following API endpoints:

* `GET /products`, which returns all products
* `GET /products/{id}`, which returns the product with the given id (using `@PathVariable`)

## e) Extend the OrderRestController

Also create the following API endpoints:

* `GET /orders`, which returns all orders
* `POST /orders`, through which a new order can be placed. The order is defined by the following JSON content:

````json
{
  "phoneNumber": "123-4567",
  "itemQuantities": {
    "S-02": 1,
    "P-10": 2,
    "P-12": 1
  }
}
````

Create a class or a record `OrderRequest` for this body with the fields `phoneNumber` (`String`) and `itemQuantities`
(`Map<String, Integer>`, product id to quantity) and accept it in the controller method with `@RequestBody`.
