package imdb.webservice.service;

import imdb.webservice.dto.MovieDocDto;
import imdb.webservice.dto.MovieDocDtoPage;
import imdb.webservice.dto.UserDto;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoviesServiceImpl implements MovieService {
    @Value("${app.urls.search-service.search}")
    private String searchUrl;
    @Value("${app.urls.search-service.test-all}")
    private String testAll;
    private final RestService restService;
    private final RestClient restClient;
    private final JwtValidationService jwtValidationService;

    @Override
    public Page<MovieDocDto> searchMovies(String query, int page, int size) {
        String url = UriComponentsBuilder.fromUri(URI.create(searchUrl))
                .queryParam("query", query)
                .queryParam("page", page)
                .queryParam("size", size)
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        String jwt = jwtValidationService.getTokenFromSession(getSession());
        headers.setBearerAuth(jwt);
        MovieDocDtoPage<MovieDocDto> response = restService.get(
                url,
                headers,
                new ParameterizedTypeReference<>() {
                }
        );
        System.out.println("Response : " + response);
        System.out.println("response content: " + response.getContent() + " " +response.getNumber());
        return new PageImpl<>(
                response.getContent(),
                PageRequest.of(response.getNumber(), response.getSize()),
                response.getTotalElements()
        );
    }

    @Override
    public List<MovieDocDto> all() {
        HttpHeaders headers = new HttpHeaders();
        String jwt = jwtValidationService.getTokenFromSession(getSession());
        headers.setBearerAuth(jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);

        MovieDocDto[] array = restClient.get()
                .uri(testAll)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .body(MovieDocDto[].class);

        return array != null ? Arrays.asList(array) : Collections.emptyList();
    }

    @Override
    public HttpSession getSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest().getSession(false); // false — не создаёт сессию, если её нет
        }
        return null;
    }
}
