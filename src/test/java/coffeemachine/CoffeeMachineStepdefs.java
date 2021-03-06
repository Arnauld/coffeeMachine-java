package coffeemachine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CoffeeMachineStepdefs {

    private BeverageQuantityChecker beverageQuantityChecker;
    private EmailNotifier emailNotifier;
    //
    private CoffeMakerGateway gateway;
    private String message;
    private BigDecimal money = new BigDecimal("2");
    private String statisticsSnapshot;
    private Scenario scenario;

    @Before
    public void intializeContext(Scenario scenario) {
        this.beverageQuantityChecker = Mockito.mock(BeverageQuantityChecker.class);
        this.emailNotifier = Mockito.mock(EmailNotifier.class);

        //
        this.scenario = scenario;
        this.gateway = new CoffeMakerGateway(emailNotifier, beverageQuantityChecker);
        this.money = new BigDecimal("2");

        // not required, but make it explicit
        when(beverageQuantityChecker.isEmpty(anyString())).thenReturn(false);
    }

    @When("^I order an? \"([^\"]*)\"$")
    public void I_order_an(String typeOfDrink) throws Throwable {
        orderDrink(typeOfDrink, 0, false);
    }

    @When("^I order an? \"([^\"]*)\" with (\\d+) sugar$")
    public void I_order_a_with_sugar(String typeOfDrink, int numberOfSugar) throws Throwable {
        orderDrink(typeOfDrink, numberOfSugar, false);
    }

    @When("^I order an extra hot \"([^\"]*)\" with (\\d+) sugar$")
    public void I_order_an_extra_hot_with_sugar(String typeOfDrink, int numberOfSugar) throws Throwable {
        orderDrink(typeOfDrink, numberOfSugar, true);
    }

//    @When("^I order a(n extra hot)? \"([^\"]*)\" with (\\d+) sugar$")
//    public void I_order_an_extra_hot_with_sugar(String extraHot, String typeOfDrink, int numberOfSugar) throws Throwable {
//        boolean isExtraHot = areEquals(extraHot, "n extra hot");
//        orderDrink(typeOfDrink, numberOfSugar, isExtraHot);
//    }

    private void orderDrink(String typeOfDrink, int numberOfSugar, boolean extraHot) {
        orderDrink(Drink.fromString(typeOfDrink), numberOfSugar, extraHot, money, new Date());
    }

    private void orderDrink(Drink drink, int numberOfSugar, boolean extraHot, BigDecimal money, Date when) {
        Order order = new Order(drink, numberOfSugar, money, extraHot, when);
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

    @Given("^the following orders:$")
    public void the_following_orders(DataTable orders) throws Throwable {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        for(Map<String,String> row : orders.asMaps()) {
            Date when = df.parse(row.get("time"));
            Drink drink = Drink.fromString(row.get("drink"));
            orderDrink(drink, 0, false, drink.price(), when);
        }
    }

    @When("^I query for a report$")
    public void I_query_for_a_report() throws Throwable {
        statisticsSnapshot = gateway.statisticsSnapshot();
    }

    @Then("^the report output should be$")
    public void the_report_ouput_should_be(String expectedStatistics) throws Throwable {
        assertThat(statisticsSnapshot).isEqualTo(expectedStatistics);
    }

    @Given("^no more \"([^\"]*)\" remaining in the machine$")
    public void no_more_in_the_machine(String typeOfDrink) throws Throwable {
        reset(beverageQuantityChecker);
        when(beverageQuantityChecker.isEmpty(eq(typeOfDrink.toLowerCase()))).thenReturn(true);
    }

    @Then("^a mail should have been sent indicating \"([^\"]*)\" is running out$")
    public void a_mail_should_have_been_sent_indicating_is_running_out(String typeOfDrink) throws Throwable {
        ArgumentCaptor<String> drinkCapture = ArgumentCaptor.forClass(String.class);
        verify(emailNotifier).notifyMissingDrink(drinkCapture.capture());
        assertThat(drinkCapture.getValue()).isEqualToIgnoringCase(typeOfDrink);
    }
}
