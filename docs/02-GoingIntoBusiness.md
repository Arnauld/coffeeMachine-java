Rename `Drink` into `Order`

```gherkin
Feature: Going into business
  In order to goes into Business
  As a shopkeeper
  I want to ensure The coffee machine is not free anymore!

Scenario: 1 tea with just enough money

  Given I've inserted 0.4€ in the machine
  When I order a "Tea" with 1 sugar
  Then the instruction generated should be "T:1:0"
``` 


```java
@Given("^I've inserted (\\d+).(\\d+)€ in the machine$")
public void I_ve_inserted_€_in_the_machine(int arg1, int arg2) throws Throwable {
    // Express the Regexp above with the code you wish you had
    throw new PendingException();
}
```

Limit of the generated code, decimal has not been detected...
Adjust the generated code and make usage of predefined converter to retrieve directly a BigDecimal.

```java

    private BigDecimal money;
    ...
    @Given("^I've inserted (\\d+\\.?\\d*)€ in the machine$")
    public void I_ve_inserted_money_in_the_machine(BigDecimal money) throws Throwable {
        this.money = money;
    }
```

Now modify our order class to take the money provided.
?? What will happen for previous scenario where no money were inserted?
One will make sure by default there is always enough money, let's say 2€.

```java
    private BigDecimal money = new BigDecimal("2");

    @When("^I order a \"([^\"]*)\" with (\\d+) sugar$")
    public void I_order_a_with_sugar(String typeOfDrink, int numberOfSugar) throws Throwable {
        DrinkType type = DrinkType.fromString(typeOfDrink);
        Order order = new Order(type, numberOfSugar, money);
        this.message = gateway.order(order);
    }
```

Every thing and the features are ... green!

Voyons le cas ou il n'y a pas assez d'argent maintenant:

```gherkin
  Scenario: 1 tea with not enough money

  If not enough money is provided, we want to send a message to the drink maker.
  The message should contains at least the amount of money missing.

    Given I've inserted 0.30€ in the machine
    When I order a "Tea" with 1 sugar
    Then the instruction generated should be "M:Not enough money 0.10 missing"

```

Passons rapidement sur une implémentation:

```java
package coffeemachine;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CoffeMakerGateway {
    public String order(Order order) {
        BigDecimal missingMoney = missingMoney(order);
        if(missingMoney.compareTo(BigDecimal.ZERO) > 0)
            return message("Not enough money " + formatMoney(missingMoney) + " missing");
        else
            return typeOfDrinkProtocolPart(order) + ":" + sugarProtocolPart(order);
    }

    private String formatMoney(BigDecimal money) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US); // decimal sep is '.'
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(money);
    }

    private BigDecimal missingMoney(Order order) {
        // TODO move method into Order?
        BigDecimal money = order.getMoney();
        BigDecimal price = order.getDrink().price();
        return price.subtract(money);
    }

    public String message(String message) {
        return "M:" + message;
    }

    private String sugarProtocolPart(Order order) {
        int numberOfSugar = order.getNumberOfSugar();
        if(numberOfSugar==0)
            return ":";
        else
            return numberOfSugar + ":0";
    }

    private String typeOfDrinkProtocolPart(Order order) {
        Drink drink = order.getDrink();
        return drink.protocolPart();
    }
}
```

Finalement après rajout de quelques cas:


```gherkin
Feature: Going into business
  In order to goes into Business
  As a shopkeeper
  I want to ensure The coffee machine is not free anymore!

  Scenario: 1 tea with just enough money

  The drink maker should make the drinks only if the correct amount of money is given

    Given I've inserted 0.40€ in the machine
    When I order a "Tea" with 1 sugar
    Then the instruction generated should be "T:1:0"

  Scenario: 1 tea with not enough money

  If not enough money is provided, we want to send a message to the drink maker.
  The message should contains at least the amount of money missing.

    Given I've inserted 0.30€ in the machine
    When I order a "Tea" with 1 sugar
    Then the instruction generated should be "M:Not enough money 0.10 missing"

  Scenario: 1 coffee with more than required money

  If too much money is given, the drink maker will still make the drink according
  to the instructions. The machine will handle the return of the correct change.

    Given I've inserted 2€ in the machine
    When I order a "Coffee" with 0 sugar
    Then the instruction generated should be "C::"

  Scenario Outline: Check missing money

    Given I've inserted <money>€ in the machine
    When I order a "<drink>" with <n> sugar
    Then the instruction generated should be "<instruction>"

  Examples:
    | money | drink     | n | instruction                     |
    | 0.25  | Coffee    | 0 | M:Not enough money 0.25 missing |
    | 0.55  | Chocolate | 0 | M:Not enough money 0.05 missing |
    | 0.05  | Tea       | 1 | M:Not enough money 0.35 missing |

``` 
