package dms.westside.portterr.alba;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfiguration {

    @Value("${auth-cookie}")
    String authCookie;

    @Bean
    public RestTemplate restTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    public HttpEntity httpEntity() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", "Alba3=" + authCookie + ";alba_an=manhattanportuguese;alba_un=daniels");
        headers.add("Accept", "application/json, text/javascript, */*; q=0.01");

        // Create HttpEntity with headers
        return new HttpEntity<>(headers);
    }
}
