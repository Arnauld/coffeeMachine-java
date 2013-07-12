package coffeemachine;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Order {
    private final Drink drink;
    private final int numberOfSugar;
    private final BigDecimal money;

    public Order(Drink drink, int numberOfSugar, BigDecimal money) {
        this.drink = drink;
        this.numberOfSugar = numberOfSugar;
        this.money = money;
    }

    public Drink getDrink() {
        return drink;
    }

    public int getNumberOfSugar() {
        return numberOfSugar;
    }

    public BigDecimal getMoney() {
        return money;
    }
}
