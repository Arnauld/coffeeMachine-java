
http://cukes.info/install-cucumber-jvm.html

`pom.xml`

```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>

      <groupId>org.technbolts</groupId>
      <artifactId>coffee-machine</artifactId>
      <version>1.0-SNAPSHOT</version>

      <properties>
        <cucumber.version>1.1.3</cucumber.version>
      </properties>

      <dependencies>
        <dependency>
          <groupId>info.cukes</groupId>
          <artifactId>cucumber-picocontainer</artifactId>
          <version>${cucumber.version}</version>
          <scope>test</scope>
        </dependency>
        <dependency>
          <groupId>info.cukes</groupId>
          <artifactId>cucumber-junit</artifactId>
          <version>${cucumber.version}</version>
          <scope>test</scope>
        </dependency>
        <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <version>4.11</version>
          <scope>test</scope>
        </dependency>
        <dependency>
          <groupId>org.assertj</groupId>
          <artifactId>assertj-core</artifactId>
          <version>1.3.0</version>
          <scope>test</scope>
        </dependency>
      </dependencies>
    </project>
```

Runner `CucumberTest.java`

```java
package coffemachine;

import cucumber.api.junit.Cucumber;

import org.junit.runner.RunWith;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Cucumber.class)
public class CucumberTest {
}
```

Feature: `01-making-drinks.feature`

```gherkin
Feature: Making Drinks
  In order to send commands to the drink maker
  As a developer
  I want to implement the logic that translates orders
  from customers of the coffee machine to the drink maker

Scenario: 1 tea with 1 sugar and a stick

  When I order a "Tea" with 1 sugar
  Then the instruction generated should be "T:1:0"
```

Run test output:

```
You can implement missing steps with the snippets below:

@When("^I order a \"([^\"]*)\" with (\\d+) sugar$")
public void I_order_a_with_sugar(String arg1, int arg2) throws Throwable {
    // Express the Regexp above with the code you wish you had
    throw new PendingException();
}

@Then("^the instruction generated should be \"([^\"]*)\"$")
public void the_instruction_generated_should_be(String arg1) throws Throwable {
    // Express the Regexp above with the code you wish you had
    throw new PendingException();
}

Wrong test finished. 
```
