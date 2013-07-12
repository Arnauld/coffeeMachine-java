package coffeemachine;

import static org.assertj.core.api.Assertions.assertThat;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CoffeeMachineStepdefs {

    private CoffeMakerGateway gateway = new CoffeMakerGateway();
    private String message;
    private BigDecimal money = new BigDecimal("2");

    @When("^I order a \"([^\"]*)\" with (\\d+) sugar$")
    public void I_order_a_with_sugar(String typeOfDrink, int numberOfSugar) throws Throwable {
        Drink type = Drink.fromString(typeOfDrink);
        Order order = new Order(type, numberOfSugar, money);
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

    @Given("^I've inserted (\\d+\\.?\\d*)€ in the machine$")
    public void I_ve_inserted_money_in_the_machine(BigDecimal money) throws Throwable {
        this.money = money;
    }

}
