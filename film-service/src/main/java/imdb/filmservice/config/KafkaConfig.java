package imdb.filmservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Value("${app.kafka.topic.search-service.load-to-elastic}")
    private String loadAllEvent;
    @Value("${app.kafka.topic.search-service.create}")
    private String createEvent;
    @Value("${app.kafka.topic.search-service.update}")
    private String updateEvent;
    @Value("${app.kafka.topic.search-service.delete}")
    private String deleteEvent;

    @Bean
    public NewTopic topicCreate() {
        return TopicBuilder.name(createEvent)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicUpdate() {
        return TopicBuilder.name(updateEvent)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicDelete() {
        return TopicBuilder.name(deleteEvent)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicLoadToElastic() {
        return TopicBuilder.name(loadAllEvent)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
