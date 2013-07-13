package coffeemachine;

import static coffeemachine.Formats.formatMoney;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CoffeMakerGateway {

    private final EmailNotifier emailNotifier;
    private final BeverageQuantityChecker beverageQuantityChecker;

    private Statistics statistics = new Statistics();

    public CoffeMakerGateway(EmailNotifier emailNotifier,
                             BeverageQuantityChecker beverageQuantityChecker)
    {
        this.emailNotifier = emailNotifier;
        this.beverageQuantityChecker = beverageQuantityChecker;
    }

    public String order(Order order) {
        String drink = order.getDrink().getAsString();
        if(beverageQuantityChecker.isEmpty(drink)) {
            // TODO how to determine an email has not already been sent
            // for this shortage
            emailNotifier.notifyMissingDrink(drink);
            return message("no more " + drink + ", select an other beverage");
        }

        BigDecimal missingMoney = missingMoney(order);
        if(missingMoney.compareTo(BigDecimal.ZERO) > 0)
            return message("Not enough money " + formatMoney(missingMoney) + " missing");
        else {
            statistics.updateStats(order);
            return typeOfDrinkProtocolPart(order) + ":" + sugarProtocolPart(order);
        }
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
        return drink.protocolPart() + (order.isExtraHot()?"h":"");
    }

    public String statisticsSnapshot() {
        return statistics.snapshot();
    }
}
