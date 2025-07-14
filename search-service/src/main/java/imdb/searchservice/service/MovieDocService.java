package imdb.searchservice.service;

import imdb.searchservice.dto.MovieDocDto;
import imdb.searchservice.dto.MovieEvent;
import org.springframework.data.domain.Page;
import java.util.List;

public interface MovieDocService {
    void reindexAllMovies(MovieEvent movieEvent);

    List<MovieDocDto> getAll();

    void handleCreate(MovieEvent event);

    void handleUpdate(MovieEvent event);

    void handleDelete(MovieEvent event);

    Page<MovieDocDto> searchMovies(String searchText, int page, int size);
}
