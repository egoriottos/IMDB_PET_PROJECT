package imdb.webservice.controller.movies;

import imdb.webservice.dto.MovieDocDto;
import imdb.webservice.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("web-service")
@RequiredArgsConstructor
@Slf4j
public class MoviesController {
    private final MovieService service;

    @GetMapping("/search")
    public String searchPage() {
        return "search-movie";
    }

    @GetMapping("/search-movie")
    public String search(
            @RequestParam("query") String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model
    ) {

        Page<MovieDocDto> moviesPage = service.searchMovies(query, page, size);
        log.info("Поиск фильмов: query={}, page={}, size={}", query, page, size);
        model.addAttribute("moviesPage", moviesPage);
        model.addAttribute("query", query);
        return "search-movie";
    }

    @GetMapping("/movies/all")
    public String all(Model model) {
        List<MovieDocDto> all = service.all();
        model.addAttribute("all", all);
        return "all-movies";
    }
}
