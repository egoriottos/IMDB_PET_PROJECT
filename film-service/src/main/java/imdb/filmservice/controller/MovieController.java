package imdb.filmservice.controller;

import imdb.filmservice.dto.MovieDto;
import imdb.filmservice.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie-service")
public class MovieController {
    private final MovieService movieService;

    @PostMapping("/create")
    public ResponseEntity<MovieDto> create(@RequestBody MovieDto dto) {
        return ResponseEntity.ok().body(movieService.createMovie(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MovieDto> update(@PathVariable Long id, @RequestBody MovieDto dto) {
        return ResponseEntity.ok().body(movieService.update(id, dto));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<MovieDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(movieService.getMovieById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getAll() {
        return ResponseEntity.ok().body(movieService.getAll());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.ok("Movie was deleted");
    }
}
