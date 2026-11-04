# Exercises for 005

Have a look at the class `PizzaApp`. It is still incomplete, but it already performs a few operations and prints their
results to `System.out`:

* looking up and printing the product with id "P-10"
* looking up and printing the customer with mobile number "+49 123 456789"
* placing an order, namely 3 times the pizza "P-10" and once the salad "S-03"

The application can be started via its `main()` method, but it still fails.

A look into the class quickly shows why -- the instantiations are still missing.

**Add the missing parts so that the application runs without errors and produces output.**

Note: we are **not using Spring** here yet! That is all still to come …

PS: What do you think this line of code does?

````
new DataLoader.Sample(productService, customerService).run();
````
