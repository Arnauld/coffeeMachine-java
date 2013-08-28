package coffeemachine;

import cucumber.api.junit.Cucumber;

import org.junit.runner.RunWith;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Cucumber.class)
@Cucumber.Options(tags = {"~@manual"},
                  format = {"html:target/html"})
public class CucumberTest {
}
