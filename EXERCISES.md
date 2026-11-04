# Exercises for chapter "130 - Testing with Boot"

So far, all tests under `src/test/java` start the complete context with `@SpringBootTest`. That is
thorough but slow, and the tests depend on the sample data of the DataLoader. In this exercise you
test the `OrderService` without Spring, with mocks in the context, and the `OrderRepository` in a
slice.

The tests pass on this branch; the two test classes for a) and b) are prepared as skeletons, their
methods still empty.

## a) Unit test with Mockito

Complete `pizza.order.OrderServiceUnitTest`. The test does without Spring entirely: it creates the
`OrderService` itself with `new`, its dependencies are mocks.

* Let Mockito create the three mocks: `@ExtendWith(MockitoExtension.class)` on the class, `@Mock`
  on the fields.
* Program the mocks with `Mockito.when(...).thenReturn(...)` or `.thenThrow(...)`. For `save()`,
  `.thenAnswer(invocation -> invocation.getArgument(0))` returns the order that was passed in.
* Use `Mockito.verify(...)` to check that the counter was increased and the order was saved, and in
  the failure case use `Mockito.verifyNoInteractions(...)` to check that the `OrderRepository` was
  not used at all.

Why does `setUp()` set the customer's id via `ReflectionTestUtils`? What happens without it?

What does Mockito report if you program something in `setUp()` that a test does not need?

## b) Mocks and spies in the context

Complete `pizza.order.OrderServiceMockBeanTest`. Here the full context runs, but two beans are
replaced:

* the `ProductService` by a mock with `@MockitoBean`: it returns the price the test specifies
* the `CustomerService` by a spy with `@MockitoSpyBean`: it calls the real methods but records the
  calls

In the second test, check that `increaseOrderCount()` is called exactly once even for a failed order
(`Mockito.verify(customerService, Mockito.times(1))`) and that the counter in the database has
really gone up. Does the spy take part in the transaction from chapter 120?

Then run all tests and count the lines `Started ...Test in` in the log. How many contexts does
Spring start, and why?

Hint: with the logger `org.springframework.test.context.cache` at `DEBUG`, Spring writes statistics
of the context cache (`size`, `hitCount`, `missCount`) after every test class. The easiest way to
set it is an environment variable, which Maven passes on to the tests:

````shell
# macOS, Linux
LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_TEST_CONTEXT_CACHE=DEBUG ./mvnw test

# Windows PowerShell
$env:LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_TEST_CONTEXT_CACHE="DEBUG"; ./mvnw test
````

`./mvnw test -Dlogging.level.org.springframework.test.context.cache=DEBUG` works as well. In
IntelliJ, enter the variable in the run configuration under *Environment variables*.

## c) A slice for JPA

Write a test `pizza.order.OrderRepositoryTest` with `@DataJpaTest` for the two queries
`findByCustomerPhoneNumber()` and `sumTotalPriceByPhoneNumber()`.

Since Spring Boot 4, `@DataJpaTest` lives in a module of its own. Add the starter
`spring-boot-starter-data-jpa-test` with scope `test` to the `pom.xml`.

Create the test data in a `@BeforeEach` method with the `TestEntityManager`, for example like this:

````java
customer = entityManager.persist(new Customer("Trans Action", null, "0931 123456"));
entityManager.persist(new Order(customer, new BigDecimal("12.50"), LocalDateTime.now()));
entityManager.persist(new Order(customer, new BigDecimal("7.40"), LocalDateTime.now()));
entityManager.flush();
entityManager.clear();
````

What are `flush()` and `clear()` for?

Answer with the help of the log and a test using `orderRepository.count()`:

* Which database does the test run against, and who creates the schema there?
* Is the sample data of the DataLoader there?
* Why doesn't a test see the data of the previous one?

## d) Bonus: Testcontainers

You can only do this part if Docker is running on your machine.

Write a test `pizza.PostgresIntegrationTest` that starts the application against a real PostgreSQL
in a container. For this you need, with scope `test`:

* `org.springframework.boot:spring-boot-testcontainers`
* `org.testcontainers:testcontainers-junit-jupiter`
* `org.testcontainers:testcontainers-postgresql`
* `org.postgresql:postgresql`
* `org.flywaydb:flyway-database-postgresql`

Spring Boot manages the versions. The container is a static field in the test class:

````java
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
class PostgresIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");
````

Check that Flyway applies the three migrations to PostgreSQL and that an order is saved. How does
Spring know the JDBC URL of the database in the container?
