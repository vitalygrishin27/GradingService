package app.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;

@RunWith(Cucumber.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@CucumberOptions(
        features = "src/test/resources/bdd",
        glue = {"classpath:app"},
        stepNotifications = true,
        tags = "not @ignore",
        plugin = {"pretty", "json:target/cucumber-report.json"})
public class BddTest {
}
