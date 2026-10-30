# Übungen zum Kapitel "120 - Transaktionen"

Die Pizzeria möchte wissen, wie oft ein Kunde zu bestellen versucht, auch wenn die Bestellung
scheitert. Dafür ist auf diesem Branch schon vorbereitet:

* die Migration `V3__add_customer_order_count.sql` mit der Spalte `order_count` in `customers`
* das Feld `orderCount` in `Customer` mit der Methode `increaseOrderCount()`
* die Methode `CustomerService#increaseOrderCount(long customerId)`, die den Kunden lädt und
  seinen Zähler erhöht
* der Test `pizza.order.OrderCountTest` mit zwei Testmethoden, noch mit `@Disabled` abgeschaltet

Die übrigen Tests unter `src/test/java` laufen auf diesem Branch grün.

## a) Bestellversuche zählen

Rufen Sie in `OrderService#placeOrder()` die Methode `increaseOrderCount()` des
`CustomerService` auf, und zwar gleich nachdem der Kunde geladen wurde, also bevor die Produkte
geladen werden.

Entfernen Sie in `OrderCountTest` beide `@Disabled` und führen Sie den Test aus. Beide
Testmethoden schlagen fehl, der Zähler bleibt bei 0. Warum wird die Änderung am Kunden nicht
gespeichert, obwohl `increaseOrderCount()` sie doch macht?

Machen Sie `CustomerService#increaseOrderCount()` mit `@Transactional` transaktional, ohne einen
Aufruf von `save()` zu ergänzen. Jetzt sind beide Tests grün. Wie kommt die Änderung in die
Datenbank?

## b) Die Bestellung als Einheit

Eine Bestellung soll ganz oder gar nicht stattfinden. Annotieren Sie deshalb auch
`OrderService#placeOrder()` mit `@Transactional`.

Führen Sie `OrderCountTest` erneut aus. Welcher Test schlägt jetzt fehl, und warum? Überlegen
Sie, in welcher Transaktion `increaseOrderCount()` jetzt läuft und was mit ihr passiert, wenn
`placeOrder()` mit der `ProductNotFoundException` endet.

## c) Der Zähler in eigener Transaktion

Ein gescheiterter Bestellversuch soll trotzdem gezählt werden. Sorgen Sie über die Propagation
von `@Transactional` dafür, dass `increaseOrderCount()` immer in einer eigenen, neuen
Transaktion läuft, die unabhängig von der Bestellung committet wird.

Jetzt müssen alle Tests grün sein. Was steht im Log zu den Transaktionen, wenn Sie in der
`application.properties` testweise
`logging.level.org.springframework.orm.jpa.JpaTransactionManager=DEBUG` setzen?

## d) Bonus: Aufruf innerhalb der Klasse

Probieren Sie aus, was passiert, wenn die neue Transaktion nicht im `CustomerService`, sondern
im `OrderService` selbst beginnt:

* Setzen Sie `increaseOrderCount()` im `CustomerService` wieder auf ein einfaches
  `@Transactional`.
* Legen Sie in `OrderService` eine `public` Methode `countOrderAttempt(long customerId)` an, die
  mit `@Transactional(propagation = Propagation.REQUIRES_NEW)` annotiert ist und
  `customerService.increaseOrderCount(customerId)` aufruft.
* Rufen Sie in `placeOrder()` statt `customerService.increaseOrderCount()` jetzt
  `countOrderAttempt()` auf.

Welcher Test schlägt fehl? Warum hat die Annotation an `countOrderAttempt()` keine Wirkung?
Nehmen Sie die Änderung danach wieder zurück.
