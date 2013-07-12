package coffeemachine;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public enum Drink {
    Tea("tea", "T", "0.4"),
    Coffee("coffee", "C", "0.5"),
    Chocolate("chocolate", "H", "0.6"),
    OrangeJuice("Orange Juice", "O", "0.6") {
        @Override
        public boolean effectiveExtraHot(boolean extraHot) {
            return false;
        }

        @Override
        public int effectiveNumberOfSugar(int numberOfSugar) {
            return 0;
        }
    };

    private final String asString;
    private final String protocol;
    private final BigDecimal price;

    Drink(String asString, String protocol, String price) {
        this.asString = asString;
        this.protocol = protocol;
        this.price = new BigDecimal(price);
    }

    public String protocolPart() {
        return protocol;
    }

    public static Drink fromString(String typeOfDrink) {
        for(Drink type : values()) {
            if(type.asString.equalsIgnoreCase(typeOfDrink))
                return type;
        }
        throw new IllegalArgumentException("Unknown type of drink '" + typeOfDrink + "'");
    }

    public BigDecimal price() {
        return price;
    }

    public int effectiveNumberOfSugar(int numberOfSugar) {
        return numberOfSugar;
    }

    public boolean effectiveExtraHot(boolean extraHot) {
        return extraHot;
    }

}
