package coffeemachine;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Formats {
    public static String formatMoney(BigDecimal money) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US); // decimal sep is '.'
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(money);
    }
}
