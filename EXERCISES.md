# Exercises for chapter "030 - Configuration"

## a) Prepare loading `application.properties`

We are using plain Spring here (no Spring Boot), so `application.properties` is not loaded automatically.

To make the values available in the `Environment` and for `@Value`:

- create an `application.properties` file
- and include it via a `@Configuration` class with `@PropertySource("classpath:application.properties")`

We build on this in the following exercises to set and read the configuration.

## b) Make the OrderService configurable

Change the `OrderService` so that the properties defined there can be set through configuration:

* expected delivery time in minutes
* the weekdays on which the discount applies
* the discount rate (in %)

Note: there are two ways to do this:

1. with the `@Value` annotation on each field
2. or via a new `OrderProperties` class that reads the values from the `Environment`

With the second approach you no longer need the fields in the class, since the values are injected through the new
`OrderProperties` class.

In the next exercise c) the values are set in the `application.properties` file and printed as a check.

## c) Print the configuration

Add a method to the `OrderService` that prints the configuration via `System.out` (so we can see what currently
applies).

Have Spring run this method automatically at startup. How did that work again ...? :)

Set values for the configuration of the `OrderService` in the `application.properties` file.

Now start the application and check the configuration actually in effect -- is the value from
`application.properties` used?

## d) Configuration from outside

Start your application in such a way that the delivery time in minutes is not taken from `application.properties`
but overridden from outside with a different value.

You can use an environment variable or a JVM system property for this.

Is the expected value printed?

## e) Optional DataLoader

Make the execution of the DataLoader configurable (on/off) - or even which DataLoader bean should run (e.g. "none",
"sample" or "csv").

There are several ways to do this that we have come across.

You can add the property you define for this to `application.properties` with the current value "sample" and
override it at runtime, e.g. with an environment variable.

> Running the `DataLoader` bean has been moved into a new `DataLoadRunner` to make the configuration easier.

## f) Resource injection -- optional

> Spring's resource handling was not part of this training; this exercise is a bonus for the curious.

One small thing can still be improved:

The CSV DataLoader still uses a `ResourceLoader` to get hold of the CSV file's resource. A resource can also be
injected directly into a bean, though. Change the CSV DataLoader so that the resource is injected directly.

Use the `@Value` annotation and the `classpath:` prefix for this.
