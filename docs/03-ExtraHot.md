```gherkin
Feature: Going into business
  In order to provide more choice and to attract more customer
  As a shopkeeper
  I want to be able to make orange juice and to deliver extra hot drinks

Scenario: An extra hot tea with 1 sugar

  When I order an extra hot "Tea" with 1 sugar
  Then the instruction generated should be "Th:1:0"
```

```java
    @When("^I order a \"([^\"]*)\" with (\\d+) sugar$")
    public void I_order_a_with_sugar(String typeOfDrink, int numberOfSugar) throws Throwable {
        orderDrink(typeOfDrink, numberOfSugar, false);
    }

    @When("^I order an extra hot \"([^\"]*)\" with (\\d+) sugar$")
    public void I_order_an_extra_hot_with_sugar(String typeOfDrink, int numberOfSugar) throws Throwable {
        orderDrink(typeOfDrink, numberOfSugar, true);
    }

    private void orderDrink(String typeOfDrink, int numberOfSugar, boolean extraHot) {
        Drink type = Drink.fromString(typeOfDrink);
        Order order = new Order(type, numberOfSugar, money, extraHot);
        this.message = gateway.order(order);
    }
```

ou une alternative (peut être moins lisible) avec une expression régulière plus générale mais du coup moins lisible.

```java
    @When("^I order a(n extra hot)? \"([^\"]*)\" with (\\d+) sugar$")
    public void I_order_an_extra_hot_with_sugar(String extraHot, String typeOfDrink, int numberOfSugar) throws Throwable {
        boolean isExtraHot = areEquals(extraHot, "n extra hot");
        orderDrink(typeOfDrink, numberOfSugar, isExtraHot);
    }
```

Dans `CoffeeMakerGateway`:

```java
    private String typeOfDrinkProtocolPart(Order order) {
        Drink drink = order.getDrink();
        return drink.protocolPart() + (order.isExtraHot()?"h":"");
    }
```


Continuons avec le Jus d'Orange

```gherkin
  Scenario: An Orange juice

    When I order an "Orange Juice"
    Then the instruction generated should be "O::"
```

Nous allons encore une fois modifier la regex pour accepter "a" ou "an" afin de rendre la phrase plus fluide:

```gherkin
When I order an "Orange Juice"
When I order a "Chocolate"
```


```java
    @When("^I order an? \"([^\"]*)\" with (\\d+) sugar$")
    public void I_order_a_with_sugar(String typeOfDrink, int numberOfSugar) throws Throwable {
        orderDrink(typeOfDrink, numberOfSugar, false);
    }
```

Ainsi qu'un nouveau Step

```
    @When("^I order an? \"([^\"]*)\"$")
    public void I_order_an(String typeOfDrink) throws Throwable {
        orderDrink(typeOfDrink, 0, false);
    }
```

Il nous reste juste à rajouter dans l'enumeration `Drink`

```java
public enum Drink {
    ...
    OrangeJuice("Orange Juice", "O", "0.6");
    ...
}
```

Rajoutons les scenarios suivants:

```gherkin
  Scenario: Extra sugar with Orange Juice is ignored

    When I order an "Orange Juice" with 1 sugar
    Then the instruction generated should be "O::"


  Scenario: Extra hot with Orange Juice is ignored

    When I order an extra hot "Orange Juice" with 1 sugar
    Then the instruction generated should be "O::"
```

Modifions l'enumeration pour rajouter le comportement voulu:

```java
public enum Drink {
    Tea("tea", "T", "0.4"),
    Coffee("coffee", "C", "0.5"),
    Chocolate("chocolate", "H", "0.6"),
    OrangeJuice("Orange Juice", "O", "0.6") {
        @Override
        public boolean effectiveExtraHot(boolean extraHot) {
            return false;
        }

        @Override
        public int effectiveNumberOfSugar(int numberOfSugar) {
            return 0;
        }
    };

    private final String asString;
    private final String protocol;
    private final BigDecimal price;

    Drink(String asString, String protocol, String price) {
        this.asString = asString;
        this.protocol = protocol;
        this.price = new BigDecimal(price);
    }

    public String protocolPart() {
        return protocol;
    }

    public static Drink fromString(String typeOfDrink) {
        for(Drink type : values()) {
            if(type.asString.equalsIgnoreCase(typeOfDrink))
                return type;
        }
        throw new IllegalArgumentException("Unknown type of drink '" + typeOfDrink + "'");
    }

    public BigDecimal price() {
        return price;
    }

    public int effectiveNumberOfSugar(int numberOfSugar) {
        return numberOfSugar;
    }

    public boolean effectiveExtraHot(boolean extraHot) {
        return extraHot;
    }

}
```

et prenons en compte ces nouvelles possibilités:

```
public class Order {
    ...

    public boolean isExtraHot() {
        return drink.effectiveExtraHot(extraHot);
    }

    public int getNumberOfSugar() {
        return drink.effectiveNumberOfSugar(numberOfSugar);
    }

    ...
}
```
