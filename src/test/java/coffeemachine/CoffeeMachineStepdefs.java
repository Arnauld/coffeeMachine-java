package coffeemachine;

import static org.assertj.core.api.Assertions.assertThat;

import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.assertj.core.api.Assertions;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CoffeeMachineStepdefs {

    private CoffeMakerGateway gateway = new CoffeMakerGateway();
    private String message;

    @When("^I order a \"([^\"]*)\" with (\\d+) sugar$")
    public void I_order_a_with_sugar(String typeOfDrink, int numberOfSugar) throws Throwable {
        DrinkType type = DrinkType.fromString(typeOfDrink);
        Drink drink = new Drink(type, numberOfSugar);
        this.message = gateway.order(drink);
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
