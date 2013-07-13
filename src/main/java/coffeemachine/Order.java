package coffeemachine;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Order {
    private final Drink drink;
    private final int numberOfSugar;
    private final BigDecimal money;
    private final boolean extraHot;

    public Order(Drink drink, int numberOfSugar, BigDecimal money, boolean extraHot, Date when) {
        this.drink = drink;
        this.numberOfSugar = numberOfSugar;
        this.money = money;
        this.extraHot = extraHot;
    }

    public boolean isExtraHot() {
        return drink.effectiveExtraHot(extraHot);
    }

    public Drink getDrink() {
        return drink;
    }

    public int getNumberOfSugar() {
        return drink.effectiveNumberOfSugar(numberOfSugar);
    }

    public BigDecimal getMoney() {
        return money;
    }
}
