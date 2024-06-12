import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.security.KeyStore;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {
        // Load CA certificate
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream caInput = getClass().getResourceAsStream("/ca.pem")) {
            trustStore.load(caInput, null);
        }

        // Load client certificate and key
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream keyInput = getClass().getResourceAsStream("/transport.p12")) {
            keyStore.load(keyInput, "your_keystore_password".toCharArray());
        }

        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(trustStore, null)
                .loadKeyMaterial(keyStore, "your_key_password".toCharArray())
                .build();

        HttpClient client = HttpClients.custom()
                .setSSLContext(sslContext)
                .build();

        return builder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(client))
                .build();
    }
}
