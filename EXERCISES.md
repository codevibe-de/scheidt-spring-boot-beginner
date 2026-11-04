# Exercises for chapter "077 - Spring Boot"

In this chapter we migrate the existing pizza application to **Spring Boot**. So far, all infrastructure has been
wired up *by hand*: creating the `ApplicationContext`, the embedded `DataSource`, the runners and loading the
properties. Spring Boot takes exactly
this hand-written boilerplate off our hands through **starter dependencies** and **auto-configuration**.

> **Starting point:** Some of this hand-written infrastructure (the embedded `DataSource` and the JDBC
> repositories) comes from chapters of the full course that are not part of this training. You don't need to know it
> in detail -- what matters here is seeing how much of it Boot makes unnecessary.

The goal of this exercise is therefore not so much to write *new* code, but above all to **delete existing code** and
replace it with what Boot provides out of the box. While cleaning up, pay attention to what happens *automatically*
as soon as the corresponding starter is on the classpath.

> **Note:** We deliberately focus on a few **core topics** of the migration (entry point, DataSource,
> runners/properties). The application stays a command-line application here -- the web layer follows in the next
> chapter together with REST.

## Setup (already prepared)

The following has **already been done** for you -- you don't need to change the build files:

- The Maven build (`pom.xml`) has been switched to Spring Boot:
  - It now uses `spring-boot-starter-parent` as its parent POM.
  - The individual `spring-*` dependencies and their versions have been replaced by **starters**:
    `spring-boot-starter-jdbc` and `spring-boot-starter-aspectj`. The versions are now managed by the Boot BOM --
    explicit `<version>` entries are gone.

From here on it's **your** turn. Work through the tasks in order and start the application after each step to see
what Boot has already taken off your hands.

## a) Entry point: `@SpringBootApplication`

Turn `PizzaApp` into a real Spring Boot application:

➡️ On the class, replace the combination of `@Configuration` + `@ComponentScan` with the single annotation
`@SpringBootApplication` (it contains both -- plus `@EnableAutoConfiguration`).

➡️ In `main(...)`, replace the manual creation of the `AnnotationConfigApplicationContext` with

```java
SpringApplication.run(PizzaApp.class, args);
```

## b) `DataSource` via auto-configuration

➡️ Delete the class `EmbeddedDataSourceConfig`.

As soon as H2 is on the classpath (`spring-boot-starter-jdbc` together with the H2 dependency), Boot automatically
configures an embedded in-memory `DataSource`. In addition, Boot runs a `schema.sql` (and optionally `data.sql`) in
the root of the classpath at startup -- exactly what we did by hand before with the `EmbeddedDatabaseBuilder`.

> **Tip:** Stay sceptical and look at the log during startup -- Boot reports the H2 `DataSource` it created and the
> execution of `schema.sql` there.

## c) Switch runners & properties to what Boot provides

We also replace the home-made runner mechanism and the manual property handling with Boot standards.

### Runners

So far there is a **custom** interface `pizza.util.CommandLineRunner`, which is invoked in `PizzaApp` via an
`@EventListener(ContextRefreshedEvent.class)`; for this, the command-line arguments are laboriously "parked" in the
context as a bean via the record `CliArgs`.

➡️ Replace the custom interface with Boot's `org.springframework.boot.CommandLineRunner`. Boot calls all beans of this
type automatically after startup and passes the command-line arguments. This makes the following **unnecessary**, and
they can be deleted:

- the `@EventListener(ContextRefreshedEvent.class)` in `PizzaApp`,
- the record `pizza.util.CliArgs`,
- the condition `pizza.util.CliArgsPresentCondition` and the `@Conditional` on `LogicRunner` that uses it.

➡️ `DataLoadRunner` and `LogicRunner` now simply implement `org.springframework.boot.CommandLineRunner`. You can keep
the `@Order` annotations to control the order.

### Properties

➡️ Turn the record `pizza.order.OrderProperties` into a `@ConfigurationProperties` class (`prefix = "app.order"`)
and enable it with `@EnableConfigurationProperties(OrderProperties.class)` (or `@ConfigurationPropertiesScan`). Boot
then binds the values from `application.properties` automatically to the record components
(`delivery-time-in-minutes`, `discount-days`, `discount-rate`).

➡️ Then delete the class `PropertiesConfig` together with its `@Bean` method and the `@PropertySource`. Boot loads
`application.properties` automatically anyway -- an explicit `@PropertySource` is no longer needed.

## Verification

Once everything is migrated:

```bash
./mvnw spring-boot:run
```

- The application starts, the `DataLoadRunner` loads the sample data and the `LogicRunner` prints its tables.
  There is no web server yet, so the application ends right afterwards.

Finally, compare **how much code you have deleted** -- that is the real message of this chapter: Spring Boot replaces
your hand-written infrastructure code with convention and auto-configuration.
