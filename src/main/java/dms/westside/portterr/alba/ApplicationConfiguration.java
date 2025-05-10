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

    @Bean
    public HttpEntity httpEntity2() {

        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", "Alba3=" + authCookie + ";alba_an=manhattanportuguese;alba_un=daniels");
        headers.add("Accept", "application/json, text/javascript, */*; q=0.01");
        headers.add("Accept-Encoding", "gzip, deflate, br, zstd");
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("Accept-Language", "en-GB,en;q=0.9,en-US;q=0.8");
        headers.add("Referer", "https://www.mcmxiv.com/alba/addresses2");
        headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");

        headers.add("Priority", "u=1, i");
        headers.add("Sec-Ch-Ua", "Chromium\";v=\"134\", \"Not:A-Brand\";v=\"24\", \"Google Chrome\";v=\"134");
        headers.add("Sec-Ch-Ua-Mobile", "?0");
        headers.add("Sec-Ch-Ua-Platform", "macOS");
        headers.add("Sec-Detch-Dest", "empty");
        headers.add("Sec-Fetch-Mode", "cors");
        headers.add("Sec-Fetch-Site", "same-origin");


        // Create HttpEntity with headers
        return new HttpEntity<>(headers);
    }

}
