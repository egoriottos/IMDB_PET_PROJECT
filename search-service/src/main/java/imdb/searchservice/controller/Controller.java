package imdb.searchservice.controller;

import imdb.searchservice.dto.MovieDocDto;
import imdb.searchservice.service.JwtValidationService;
import imdb.searchservice.service.MovieDocService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("search-service")
public class Controller {
    private final MovieDocService service;
    private final JwtValidationService jwtValidationService;

    @GetMapping("/test-all")
    public ResponseEntity<List<MovieDocDto>> getAll(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is missing or invalid");
        }
        String token = authHeader.substring(7);
        System.out.println("Токен для получения списка фильмов:" + token);
        if (!jwtValidationService.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
        List<MovieDocDto> movies = service.getAll();
        return ResponseEntity.ok(movies);

    }


    @GetMapping("/search")
    public Page<MovieDocDto> searchMovies(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is missing or invalid");
        }
        String token = authHeader.substring(7);
        System.out.println("Получен токен для поиска фильмов: " + token);
        if (!jwtValidationService.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
        return service.searchMovies(query, page, size);
    }
}
