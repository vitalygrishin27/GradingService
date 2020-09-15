package app.bdd.step;

import app.GradingServiceMain;
import app.bdd.config.TestApplication;
import app.bdd.converter.HeaderSettingsRequestCallback;
import app.bdd.converter.ResponseResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ContextConfiguration(loader = SpringBootContextLoader.class, classes = {GradingServiceMain.class, TestApplication.class})
@ComponentScan(basePackages = "app")
@ActiveProfiles("TEST")
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CommonStepDefinition {
    static ResponseResults response = null;

    @Autowired
    protected RestTemplate restTemplate;

    void executeGet(String url) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("gradingServiceAccessToken", "test");
        final HeaderSettingsRequestCallback requestCallback = new HeaderSettingsRequestCallback(headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        response = restTemplate.execute(url, HttpMethod.GET, requestCallback, response -> {
            if (errorHandler.hadError) {
                return errorHandler.getResults();
            }
            return new ResponseResults(response,false);
        });
    }

    void executeDelete(String url) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("gradingServiceAccessToken", "test");
        final HeaderSettingsRequestCallback requestCallback = new HeaderSettingsRequestCallback(headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        response = restTemplate.execute(url, HttpMethod.DELETE, requestCallback, response -> {
            if (errorHandler.hadError) {
                return errorHandler.getResults();
            }
            return new ResponseResults(response,false);
        });
    }

    void executePost(String url, String fileName) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("gradingServiceAccessToken", "test");
        final HeaderSettingsRequestCallback requestCallback = new HeaderSettingsRequestCallback(headers);
        try (InputStream in = getClass().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String fileContent = reader.lines().collect(Collectors.joining());
            requestCallback.setBody(fileContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }

        restTemplate.setErrorHandler(errorHandler);
        response = restTemplate.execute(url, HttpMethod.POST, requestCallback, response -> {
            if (errorHandler.hadError) {
                return errorHandler.getResults();
            }
            return new ResponseResults(response,false);
        });
    }

    void executePut(String url, String fileName) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("gradingServiceAccessToken", "test");
        final HeaderSettingsRequestCallback requestCallback = new HeaderSettingsRequestCallback(headers);
        try (InputStream in = getClass().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String fileContent = reader.lines().collect(Collectors.joining());
            requestCallback.setBody(fileContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }

        restTemplate.setErrorHandler(errorHandler);
        response = restTemplate.execute(url, HttpMethod.PUT, requestCallback, response -> {
            if (errorHandler.hadError) {
                return errorHandler.getResults();
            }
            return new ResponseResults(response,false);
        });
    }

    private class ResponseResultErrorHandler implements ResponseErrorHandler {
        private ResponseResults results = null;
        private Boolean hadError = false;

        private ResponseResults getResults() {
            return results;
        }

        @Override
        public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
            return clientHttpResponse.getRawStatusCode() >= 400;
        }

        @Override
        public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
            hadError=true;
            results = new ResponseResults(clientHttpResponse,true);
        }
    }
}
