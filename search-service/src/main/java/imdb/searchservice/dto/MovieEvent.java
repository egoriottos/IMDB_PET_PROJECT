package imdb.searchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieEvent {
    private Long id;
    private Integer rating;
    private String movie;
    private Integer year;
    private String country;
    private Float ratingBall;
    private String overview;
    private String director;
    private List<String> screenwriter;
    private List<String> actors;
    private String urlLogo;
}
