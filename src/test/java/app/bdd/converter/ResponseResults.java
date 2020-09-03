package app.bdd.converter;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class ResponseResults {
    private final ClientHttpResponse response;
    private final String body;

    public ResponseResults(final ClientHttpResponse response, boolean hadError) throws IOException {
        if (hadError) {
            this.response = response;
            this.body=null;
        } else {
            this.response = response;
            final InputStream bodyInputStream = response.getBody();
            final StringWriter stringWriter = new StringWriter();
            IOUtils.copy(bodyInputStream, stringWriter);
            this.body = stringWriter.toString();
        }
    }

    public ClientHttpResponse getResponse() {
        return response;
    }

    public String getBody() {
        return body;
    }
}
