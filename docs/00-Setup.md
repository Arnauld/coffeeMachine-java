
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

