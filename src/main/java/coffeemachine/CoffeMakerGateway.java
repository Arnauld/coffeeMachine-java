package coffeemachine;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CoffeMakerGateway {
    public String order(Drink drink) {
        return typeOfDrinkProtocolPart(drink) + ":" + sugarProtocolPart(drink);
    }

    public String message(String message) {
        return "M:" + message;
    }

    private String sugarProtocolPart(Drink drink) {
        int numberOfSugar = drink.getNumberOfSugar();
        if(numberOfSugar==0)
            return ":";
        else
            return numberOfSugar + ":0";
    }

    private String typeOfDrinkProtocolPart(Drink drink) {
        DrinkType drinkType = drink.getType();
        return drinkType.protocolPart();
    }
}
