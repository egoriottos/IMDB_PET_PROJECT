package imdb.webservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestTemplateConfiguration {
    @Value("${app.urls.user-service}")
    private String userServiceUrl;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .baseUrl(userServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
