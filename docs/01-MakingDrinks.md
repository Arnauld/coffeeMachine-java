Feature: `01-making-drinks.feature`

```gherkin
Feature: Making Drinks
  In order to send commands to the drink maker
  As a developer
  I want to implement the logic that translates orders
  from customers of the coffee machine to the drink maker

Scenario: 1 tea with 1 sugar and a stick

  When I order a "Tea" with 1 sugar
  Then the instruction generated should be "T:1:0"
```

Run test output:

```
You can implement missing steps with the snippets below:

@When("^I order a \"([^\"]*)\" with (\\d+) sugar$")
public void I_order_a_with_sugar(String arg1, int arg2) throws Throwable {
    // Express the Regexp above with the code you wish you had
    throw new PendingException();
}

@Then("^the instruction generated should be \"([^\"]*)\"$")
public void the_instruction_generated_should_be(String arg1) throws Throwable {
    // Express the Regexp above with the code you wish you had
    throw new PendingException();
}

Wrong test finished. 
```



```
package coffeemachine;

import static org.assertj.core.api.Assertions.assertThat;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CoffeeMachineStepdefs {

    private CoffeMakerGateway gateway = new CoffeMakerGateway();
    private String message;

    @When("^I order a \"([^\"]*)\" with (\\d+) sugar$")
    public void I_order_a_with_sugar(String typeOfDrink, int numberOfSugar) throws Throwable {
        DrinkType type = DrinkType.fromString(typeOfDrink);
        Order order = new Order(type, numberOfSugar);
        this.message = gateway.order(order);
    }

    @When("^the message \"([^\"]*)\" is sent$")
    public void the_message_is_sent(String message) throws Throwable {
        this.message = gateway.message(message);
    }

    @Then("^the instruction generated should be \"([^\"]*)\"$")
    public void the_instruction_generated_should_be(String protocolExpression) throws Throwable {
        assertThat(message).isEqualTo(protocolExpression);
    }

}
```


