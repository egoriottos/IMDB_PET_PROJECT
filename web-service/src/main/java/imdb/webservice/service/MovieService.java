package imdb.webservice.service;

import imdb.webservice.dto.MovieDocDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import java.util.List;

public interface MovieService {
    Page<MovieDocDto> searchMovies(String query, int page, int size);

    List<MovieDocDto> all();

    HttpSession getSession();
}
