# Exercises for chapter "120 - Transactions"

The pizzeria wants to know how often a customer tries to order, even when the order fails. The
following is already prepared on this branch:

* the migration `V3__add_customer_order_count.sql` with the column `order_count` in `customers`
* the field `orderCount` in `Customer` with the method `increaseOrderCount()`
* the method `CustomerService#increaseOrderCount(long customerId)`, which loads the customer and
  increases their counter
* the test `pizza.order.OrderCountTest` with two test methods, still switched off with `@Disabled`

The other tests under `src/test/java` pass on this branch.

## a) Counting order attempts

In `OrderService#placeOrder()`, call the method `increaseOrderCount()` of the `CustomerService`,
right after the customer has been loaded, i.e. before the products are loaded.

Remove both `@Disabled` in `OrderCountTest` and run the test. Both test methods fail, the counter
stays at 0. Why is the change to the customer not saved, even though `increaseOrderCount()` makes
it?

Make `CustomerService#increaseOrderCount()` transactional with `@Transactional`, without adding a
call to `save()`. Now both tests pass. How does the change get into the database?

## b) The order as a unit

An order should happen completely or not at all. So annotate `OrderService#placeOrder()` with
`@Transactional` as well.

Run `OrderCountTest` again. Which test fails now, and why? Think about which transaction
`increaseOrderCount()` runs in now, and what happens to it when `placeOrder()` ends with the
`ProductNotFoundException`.

## c) The counter in its own transaction

A failed order attempt should be counted anyway. Use the propagation of `@Transactional` to make
sure that `increaseOrderCount()` always runs in its own, new transaction, which is committed
independently of the order.

Now all tests must pass. What does the log say about the transactions if you set
`logging.level.org.springframework.orm.jpa.JpaTransactionManager=DEBUG` in
`application.properties` for a test run?

## d) Bonus: calls within the class

Try out what happens if the new transaction does not start in the `CustomerService` but in the
`OrderService` itself:

* Set `increaseOrderCount()` in the `CustomerService` back to a plain `@Transactional`.
* In `OrderService`, create a `public` method `countOrderAttempt(long customerId)` that is
  annotated with `@Transactional(propagation = Propagation.REQUIRES_NEW)` and calls
  `customerService.increaseOrderCount(customerId)`.
* In `placeOrder()`, call `countOrderAttempt()` instead of `customerService.increaseOrderCount()`.

Which test fails? Why does the annotation on `countOrderAttempt()` have no effect? Undo the change
afterwards.
