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
        return drink.protocolPart() + (order.isExtraHot()?"h":"");
    }
}
