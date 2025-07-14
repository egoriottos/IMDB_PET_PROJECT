package imdb.filmservice.service;

import imdb.filmservice.dto.MovieDto;
import imdb.filmservice.dto.MovieEvent;
import imdb.filmservice.entity.Movie;
import imdb.filmservice.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final KafkaTemplate<String, MovieEvent> kafkaTemplate;
    private final MovieRepository repository;
    private final ModelMapper mapper;

    @Value("${app.kafka.topic.search-service.create}")
    private String createTopic;

    @Value("${app.kafka.topic.search-service.update}")
    private String updateTopic;

    @Value("${app.kafka.topic.search-service.delete}")
    private String deleteTopic;

    @Value("${app.kafka.topic.search-service.load-to-elastic}")
    private String loadAllEvent;

    @Override
    public MovieDto getMovieById(Long id) {
        Movie movie = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return mapper.map(movie, MovieDto.class);
    }

    @Override
    public List<MovieDto> getAll() {
        List<Movie> movies = repository.findAll();
        return movies.stream().map(m -> mapper.map(m, MovieDto.class)).toList();
    }

    @Override
    public MovieDto createMovie(MovieDto movieDto) {
        Movie movie = Movie.builder()
                .rating(movieDto.getRating())
                .movie(movieDto.getMovie())
                .year(movieDto.getYear())
                .country(movieDto.getCountry())
                .ratingBall(movieDto.getRatingBall())
                .overview(movieDto.getOverview())
                .director(movieDto.getDirector())
                .screenwriter(movieDto.getScreenwriter())
                .actors(movieDto.getActors())
                .urlLogo(movieDto.getUrlLogo())
                .build();
        repository.save(movie);

        kafkaTemplate.send(createTopic, mapper.map(movie, MovieEvent.class));

        return mapper.map(movie, MovieDto.class);
    }

    @Override
    public MovieDto update(Long id, MovieDto movieDto) {
        Movie movie = repository.findById(id).
                orElseThrow(EntityNotFoundException::new);
        movie.setRating(movieDto.getRating());
        movie.setMovie(movieDto.getMovie());
        movie.setYear(movieDto.getYear());
        movie.setCountry(movieDto.getCountry());
        movie.setRatingBall(movieDto.getRatingBall());
        movie.setOverview(movieDto.getOverview());
        movie.setDirector(movieDto.getDirector());
        movie.setScreenwriter(movieDto.getScreenwriter());
        movie.setActors(movieDto.getActors());
        movie.setUrlLogo(movieDto.getUrlLogo());
        repository.save(movie);

        kafkaTemplate.send(updateTopic, mapper.map(movie, MovieEvent.class));

        return mapper.map(movie, MovieDto.class);
    }

    @Override
    public void delete(Long id) {
        Movie movie = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        repository.deleteById(movie.getId());
        kafkaTemplate.send(deleteTopic, mapper.map(movie, MovieEvent.class));
    }

    @Override
    public List<MovieEvent> getAllForKafka() {
        return repository.findAll().stream()
                .map(movie -> mapper.map(movie, MovieEvent.class)).toList();
    }

    @Override
    public void sendBulkLoadEvent(List<MovieEvent> events) {
        for (MovieEvent event : events) {
            kafkaTemplate.send(loadAllEvent, String.valueOf(event.getId()), event);
        }
    }

}
