package dms.westside.portterr.alba;

import com.fasterxml.jackson.databind.ObjectMapper;
import dms.westside.portterr.alba.dto.AlbaResponse;
import dms.westside.portterr.alba.util.Logger;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Set;

@SpringBootApplication(exclude = {ServletWebServerFactoryAutoConfiguration.class, WebMvcAutoConfiguration.class})
@EnableConfigurationProperties
public class AlbaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlbaApplication.class, args);
	}

	@Autowired
	AlbaService service;

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			service.doTheDeed();
		};
	}

	public static void log(String s) {
		System.out.println(s);
	}








}
