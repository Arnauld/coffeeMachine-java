package coffeemachine;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Drink {
    private final DrinkType type;
    private final int numberOfSugar;

    public Drink(DrinkType type, int numberOfSugar) {
        this.type = type;
        this.numberOfSugar = numberOfSugar;
    }

    public DrinkType getType() {
        return type;
    }

    public int getNumberOfSugar() {
        return numberOfSugar;
    }
}
