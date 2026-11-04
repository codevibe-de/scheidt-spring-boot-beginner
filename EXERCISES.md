# Exercises for chapter "115 - Database Migration with Flyway"

So far, Spring Boot creates the database schema from `schema.sql` on every start. That only works
as long as the database lives in memory and disappears with the application. A real database keeps
its data, and its schema has to grow with every new version of the application. In this exercise
you describe these changes as Flyway migrations.

The tests under `src/test/java` pass on this branch. Run them after every step: they must stay
green.

## a) Adding Flyway

Add the starter `spring-boot-starter-flyway` to the `pom.xml`.

Create the directory `src/main/resources/db/migration` and move `schema.sql` there, renamed to
`V1__create_tables.sql`. Remove the `DROP TABLE` statements while you are at it. Why don't they
belong in a migration?

Pay attention to the file name: capital `V`, version number, **two** underscores, description.

Then do one full rebuild (`./mvnw clean`). Otherwise the old `schema.sql` is still in
`target/classes`, and Boot runs it after Flyway.

Run the tests and look for Flyway's lines in the log. Which migration was applied to which schema?

## b) Persistent database

Start the H2 server with `start-h2-server.sh` (or `start-h2-server.bat`). It stores the database
under `h2-data/` and keeps it across restarts.

Create a profile `h2server` for running against this server, i.e. a file
`application-h2server.properties`, with

````properties
spring.datasource.url=jdbc:h2:tcp://localhost:9092/./pizzadb
spring.datasource.username=sa
spring.datasource.password=
app.data-loader=none
````

Why would the DataLoader `sample` be a bad choice here? Think of the second start.

Start `PizzaApp` with the profile `h2server` and open the H2 console at http://localhost:8082
(JDBC URL as above, user `sa`, no password). What is in the table `flyway_schema_history`?
Flyway creates the table in lower case, so in H2 its name needs quotes:
`SELECT * FROM "flyway_schema_history"`.

Start the application a second time. What does Flyway report now?

Finally, change a small detail in `V1__create_tables.sql`, such as the length of a column, and
start again. Read the error message and then undo the change.

## c) Changing the schema

Customers should be able to have an email address from now on. It is optional.

Add a field `email` with getter and setter to `Customer` and start the application with the
profile `h2server`. Which part of the application complains, and why?

Write the matching migration `V2__add_customer_email.sql` that adds the column `email` as
`VARCHAR(100)` to `customers`. Start again and look at `flyway_schema_history`. Do the tests still
pass?

## d) Bonus: sample data as a repeatable migration

Against the H2 server, the application now starts without products and customers. The sample data
should be available there again, but only there: the tests bring their data via the DataLoader.

For this, create a second directory `src/main/resources/db/sample-data` containing a repeatable
migration `R__sample_data.sql`. It should create the six products and two customers of the
DataLoader `sample`, in a way that it can run any number of times. In H2 you can do this with

````sql
MERGE INTO products (pk, name, price) KEY (pk) VALUES ('P-10', 'Pizza Margarita', 5.50);
````

Add the directory to the list of migration locations only in the profile `h2server`, via the
property `spring.flyway.locations`. Start twice. When does a repeatable migration run again? Try
it out by changing a price.
