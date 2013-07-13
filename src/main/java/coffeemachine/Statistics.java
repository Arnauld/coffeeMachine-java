package coffeemachine;

import static coffeemachine.Formats.formatMoney;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Statistics {

    private Map<Drink,Integer> stats = new HashMap<Drink, Integer>();
    private BigDecimal total = BigDecimal.ZERO;

    public void updateStats(Order order) {
        Drink drink = order.getDrink();

        Integer count = stats.get(drink);
        if(count==null) {
            count = 0;
        }
        stats.put(drink, count + 1);
        total = total.add(drink.price());
    }

    public String snapshot() {
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
