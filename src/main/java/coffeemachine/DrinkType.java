package coffeemachine;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public enum DrinkType {
    Tea("tea", "T"),
    Coffee("coffee", "C"),
    Chocolate("chocolate", "H");

    private final String asString;
    private final String protocol;

    DrinkType(String asString, String protocol) {
        this.asString = asString;
        this.protocol = protocol;
    }

    public String protocolPart() {
        return protocol;
    }

    public static DrinkType fromString(String typeOfDrink) {
        for(DrinkType type : values()) {
            if(type.asString.equalsIgnoreCase(typeOfDrink))
                return type;
        }
        throw new IllegalArgumentException("Unknown type of drink '" + typeOfDrink + "'");
    }

}
