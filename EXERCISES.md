# Exercises for 025 "Annotation-Config"

> **Starting point:** `PizzaApp` still creates all objects by hand, exactly as in the solution of 005. Spring is
> already on the classpath (`spring-context` in the `pom.xml`), but not used yet.

This exercise has **two phases**:

## Phase 1

We want Spring to find all beans through annotations from now on.

So we need to:

1. replace the manual instantiation in `PizzaApp.main()` with an `AnnotationConfigApplicationContext` and get the
   service beans from it
2. declare an elegant entry point into our application that triggers a component scan (`@Configuration` and
   `@ComponentScan`)
3. add the required bean-defining annotations to the various classes. You are free to choose whether you do this with
   stereotypes or with bean methods. Or a mix of both …

## Phase 2 -- optional

How do we now work with the service beans?

We can leave the execution of the business logic as it is, or we use the provided `pizza.util.CommandLineRunner`
interface. This works as follows:

1. Create a new class `LogicRunner` (or any other name) in the package `pizza` that implements the
   `CommandLineRunner` interface.
2. Annotate it as a bean (e.g. `@Component`) so that Spring recognises the class as a bean.
3. Move the business logic into the `run()` method of this class.
4. Autowire the required service beans into this class.
5. In the `PizzaApp.main()` method, look up all beans of type `CommandLineRunner` and call their `run()` methods.
   This is particularly easy (including ordering) with `BeanFactory.getBeanProvider(java.lang.Class<T>)` and its
   `orderedStream()` method. That way we can add more runners later and have them run in the order we want.
