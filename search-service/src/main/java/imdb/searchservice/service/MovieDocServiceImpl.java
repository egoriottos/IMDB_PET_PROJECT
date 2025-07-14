package imdb.searchservice.service;

import imdb.searchservice.dto.MovieDocDto;
import imdb.searchservice.dto.MovieEvent;
import imdb.searchservice.entity.MovieDoc;
import imdb.searchservice.repository.MovieDocRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieDocServiceImpl implements MovieDocService {
    private final MovieDocRepository repository;
    private final ModelMapper modelMapper;

    @KafkaListener(topics = "${app.kafka.topic.search-service.load-to-elastic}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void reindexAllMovies(MovieEvent movieEvent) {
        MovieEvent cleanedEvent = new MovieEvent();

        cleanedEvent.setId(movieEvent.getId());
        cleanedEvent.setRating(movieEvent.getRating());
        cleanedEvent.setMovie(movieEvent.getMovie().trim());
        cleanedEvent.setYear(movieEvent.getYear());
        cleanedEvent.setCountry(movieEvent.getCountry().trim());
        cleanedEvent.setRatingBall(movieEvent.getRatingBall());
        cleanedEvent.setOverview(movieEvent.getOverview().trim());
        cleanedEvent.setDirector(movieEvent.getDirector().trim());

        cleanedEvent.setScreenwriter(movieEvent.getScreenwriter().stream()
                .map(String::trim)
                .collect(Collectors.toList()));

        cleanedEvent.setActors(movieEvent.getActors().stream()
                .map(String::trim)
                .collect(Collectors.toList()));

        cleanedEvent.setUrlLogo(movieEvent.getUrlLogo().trim());

        repository.save(modelMapper.map(cleanedEvent, MovieDoc.class));
        System.out.println("Movie loaded to Elasticsearch: " + movieEvent.getId());
    }

    @Override
    public List<MovieDocDto> getAll() {
        return repository.findAllByOrderByRatingBallDesc().stream()
                .map(o->modelMapper.map(o, MovieDocDto.class)).toList();
    }

    @Override
    public Page<MovieDocDto> searchMovies(String searchText, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String decodeQuery = URLDecoder.decode(searchText, StandardCharsets.UTF_8);
        log.info("Декодированный запрос: {}", decodeQuery);
        Page<MovieDoc> searchResults = repository.searchByQuery(decodeQuery, pageable);
        log.info("Найдено {} результатов по запросу '{}'",
                searchResults.getTotalElements(), decodeQuery);
        return searchResults.map(o -> modelMapper.map(o, MovieDocDto.class));
    }

    @KafkaListener(topics = "${app.kafka.topic.search-service.create}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void handleCreate(MovieEvent event) {
        repository.save(modelMapper.map(event, MovieDoc.class));
        System.out.println("Movie created in Elasticsearch: " + event.getId());
    }

    @Override
    @KafkaListener(topics = "${app.kafka.topic.search-service.update}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUpdate(MovieEvent event) {
        repository.save(modelMapper.map(event, MovieDoc.class));
        System.out.println("Movie updated in Elasticsearch: " + event.getId());
    }

    @Override
    @KafkaListener(topics = "${app.kafka.topic.search-service.delete}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleDelete(MovieEvent event) {
        repository.deleteById(event.getId());
        System.out.println("Movie deleted from Elasticsearch: " + event.getId());
    }
}
