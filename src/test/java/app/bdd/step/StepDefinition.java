package app.bdd.step;

import app.bdd.dto.ResponseDto;
import app.entity.AccessToken;
import app.entity.Configuration;
import app.entity.Role;
import app.entity.User;
import app.service.AccessTokenService;
import app.service.ConfigurationService;
import app.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Slf4j
public class StepDefinition extends CommonStepDefinition {

    private static String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccessTokenService accessTokenService;

    @Value("${server.port}")
    private String port;

    @DataTableType
    public ResponseDto defineResponse(Map<String, String> entry) {
        ResponseDto result = new ResponseDto();
        result.setBody(entry.get("body"));
        return result;
    }

    @DataTableType
    public Configuration defineConfiguration(Map<String, String> entry) {
        Configuration result = new Configuration();
        result.setConfigKey(entry.get("configKey"));
        result.setConfigValue(entry.get("configValue"));
        return result;
    }

    @DataTableType
    public User defineUser(Map<String, String> entry) {
        User result = new User();
        result.setLogin(entry.get("login"));
        result.setPassword(entry.get("password"));
        result.setEncryptedPassword(entry.get("encryptedPassword"));
        result.setFirstName(entry.get("firstName"));
        result.setRole(Role.valueOf(entry.get("role")));
        return result;
    }

    @DataTableType
    public AccessToken defineToken(Map<String, String> entry) {
        AccessToken result = new AccessToken();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        result.setToken(entry.get("token"));
        User user = userService.findByLogin(entry.get("login"));
        result.setUser(user);
        result.setDateFrom(LocalDateTime.parse(entry.get("dateFrom"), formatter));
        result.setDateEnd(LocalDateTime.parse(entry.get("dateEnd"), formatter));
        return result;
    }

    @Given("Application started")
    @Transactional
    @Modifying
    public void applicationStarted() {
        accessTokenService.deleteAll();
        configurationService.deleteAll();
        userService.deleteAll();
    }

    @Given("existing Configuration")
    public void putConfigurationIntoDB(DataTable dataTable) {
        List<Configuration> configurations = new ArrayList<>(dataTable.asList(Configuration.class));
        configurations.forEach(configuration -> {
            configurationService.save(configuration);
        });
    }

    @Given("existing User")
    public void putUserIntoDB(DataTable dataTable) {
        List<User> users = new ArrayList<>(dataTable.asList(User.class));
        users.forEach(user -> {
            userService.save(user);
        });
    }
    @Given("existing Token")
    public void putTokenIntoDB(DataTable dataTable) {
        List<AccessToken> accessTokens = new ArrayList<>(dataTable.asList(AccessToken.class));
        accessTokens.forEach(accessToken -> {
            accessTokenService.save(accessToken);
        });
    }

    private void compareConfiguration(Configuration actualConfiguration, Configuration expectedConfiguration) {
        Assertions.assertThat(expectedConfiguration.getConfigKey())
                .overridingErrorMessage("Expected Configuration with id %s to have ConfigKey %s but had %s",
                        expectedConfiguration.getId(), expectedConfiguration.getConfigKey(), actualConfiguration.getConfigKey())
                .isEqualTo(actualConfiguration.getConfigKey());
        Assertions.assertThat(expectedConfiguration.getConfigValue())
                .overridingErrorMessage("Expected Configuration with id %s to have ConfigValue %s but had %s",
                        expectedConfiguration.getId(), expectedConfiguration.getConfigValue(), actualConfiguration.getConfigValue())
                .isEqualTo(actualConfiguration.getConfigValue());
    }

    @When("called DELETE method for {string}")
    public void callDelete(String url) {
        executeDelete("http://localhost:" + port + url);
    }

    @When("called GET method for {string}")
    public void callGet(String url) {
        executeGet("http://localhost:" + port + url);
    }

    @When("called POST method for {string} with request content {string}")
    public void callPost(String url, String fileName) {
        executePost("http://localhost:" + port + url, fileName);
    }

    @When("called PUT method for {string} with request content {string}")
    public void callPut(String url, String fileName) {
        executePut("http://localhost:" + port + url, fileName);
    }

    @Then("response contains status code {int}")
    public void responseContainsStatusCode(int statusCode) throws IOException {
        int currentStatusCode = response.getResponse().getStatusCode().value();
        assertThat("status code is incorrect : " + response.getBody(), currentStatusCode, is(statusCode));
    }

    @And("assert that Configuration is")
    public void configurationIs(DataTable dataTable) {
        List<Configuration> configurations = new ArrayList<>(dataTable.asList(Configuration.class));
        for (Configuration configuration : configurations) {
            Configuration actualConfiguration = configurationService.findByConfigKey(configuration.getConfigKey());
            if (actualConfiguration !=null) {
                compareConfiguration(actualConfiguration, configuration);
            } else {
                Assertions.assertThat(false)
                        .overridingErrorMessage("Expected Configuration with ConfigKey %s does not exist", configuration.getConfigKey())
                        .isTrue();
            }
        }
    }

    @And("assert that Configuration not exists")
    public void configurationNotExists(DataTable dataTable) {
        List<Configuration> configurations = new ArrayList<>(dataTable.asList(Configuration.class));
        for (Configuration configuration : configurations) {
            Configuration actualConfiguration = configurationService.findByConfigKey(configuration.getConfigKey());
            if (actualConfiguration !=null) {
                Assertions.assertThat(false)
                        .overridingErrorMessage("Expected Configuration with ConfigKey %s does not exist", configuration.getConfigKey())
                        .isTrue();
            }
        }
    }

    @And("response contains")
    public void responseContains(DataTable responseDto) {
        List<ResponseDto> responseDtos = new ArrayList<>(responseDto.asList(ResponseDto.class));
        assertThat(response.getBody(), is(responseDtos.stream().findFirst().get().getBody()));
    }

    @And("response contains Configuration")
    public void responseContainsConfiguration(DataTable dataTable) throws IOException {
        List<Configuration> configurations = new ArrayList<>(dataTable.asList(Configuration.class));
        List<Configuration> configurationFromJson = getConfigurationFromJson(response.getBody());
        configurations.forEach(configuration -> compareConfiguration(
                configurationFromJson.stream().filter(configuration1 -> configuration.getConfigKey().equals(configuration1.getConfigKey())).findFirst().get(), configuration));
    }

    private List<Configuration> getConfigurationFromJson(String jsonStr) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JsonNode rootNode = mapper.readTree(jsonStr);
        List<Configuration> configurations = new ArrayList<>();
        if (rootNode instanceof ArrayNode) {
            configurations.addAll(mapper.readValue(rootNode.toString(), new TypeReference<List<Configuration>>() {}));
        } else if (rootNode instanceof JsonNode) {
            configurations.add(mapper.readValue(rootNode.toString(), Configuration.class));
        }
        return configurations;
    }
}
