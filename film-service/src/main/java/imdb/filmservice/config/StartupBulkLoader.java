package imdb.filmservice.config;

import imdb.filmservice.dto.MovieEvent;
import imdb.filmservice.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StartupBulkLoader {
    private final MovieService service;

    @EventListener(ApplicationReadyEvent.class)
    public void sendAllMoviesToSearchService() {
        List<MovieEvent> events = service.getAllForKafka();
        service.sendBulkLoadEvent(events);
        System.out.println("✔ Все фильмы отправлены в search-service для индексации.");
    }
}
