package coffeemachine;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CoffeMakerGateway {

    private final EmailNotifier emailNotifier;
    private final BeverageQuantityChecker beverageQuantityChecker;

    private final Map<Drink,Integer> stats = new HashMap<Drink, Integer>();
    private BigDecimal total = BigDecimal.ZERO;

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
            updateStats(order);
            return typeOfDrinkProtocolPart(order) + ":" + sugarProtocolPart(order);
        }
    }

    private void updateStats(Order order) {
        Drink drink = order.getDrink();

        Integer count = stats.get(drink);
        if(count==null) {
            count = 0;
        }
        stats.put(drink, count + 1);
        total = total.add(drink.price());
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
        return drink.protocolPart() + (order.isExtraHot()?"h":"");
    }

    public String statisticsSnapshot() {
        StringBuilder builder = new StringBuilder();
        for(Drink drink : Drink.allInAphbeticalOrder()) {
            Integer count = stats.get(drink);
            if(count != null) {
                builder.append(drink.getAsString().toLowerCase()) //
                       .append(": ").append(count).append("\n");
            }
        }
        builder.append("---\n");
        builder.append("Total: ").append(formatMoney(total)).append("€");
        return builder.toString();
    }
}
