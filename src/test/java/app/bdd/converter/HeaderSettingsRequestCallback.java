package app.bdd.converter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RequestCallback;

import java.io.IOException;
import java.util.Map;

public class HeaderSettingsRequestCallback implements RequestCallback {
    final Map<String, String> requestHeaders;
    private String body;

    public HeaderSettingsRequestCallback(final Map<String, String> headers) {
        this.requestHeaders = headers;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    @Override
    public void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException {
        final HttpHeaders clientHeaders = clientHttpRequest.getHeaders();
        requestHeaders.forEach((key, value) -> clientHeaders.add(key, value));
        if (null != body) {
            clientHttpRequest.getBody().write(body.getBytes());
        }
    }
}
