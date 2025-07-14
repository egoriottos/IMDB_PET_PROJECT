package imdb.filmservice.service;

import imdb.filmservice.dto.MovieDto;
import imdb.filmservice.dto.MovieEvent;
import java.util.List;

public interface MovieService {
    MovieDto getMovieById(Long id);
    List<MovieDto> getAll();
    MovieDto createMovie(MovieDto movieDto);
    MovieDto update(Long id, MovieDto movieDto);
    void delete(Long id);
    List<MovieEvent> getAllForKafka();
    void sendBulkLoadEvent(List<MovieEvent> events);

}
