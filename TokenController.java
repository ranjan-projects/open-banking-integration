import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class TokenController {

    @Autowired
    private WebClient webClient;

    @PostMapping("/getToken")
    public String getToken() {
        String clientId = "YOUR_CLIENT_ID";
        String clientSecret = "YOUR_CLIENT_SECRET";
        String url = "https://as1.obie.uk.ozoneapi.io/token";

        String response = webClient.post()
                .uri(url)
                .headers(headers -> headers.setBasicAuth(clientId, clientSecret))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("scope", "accounts openid"))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
