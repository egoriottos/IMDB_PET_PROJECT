package imdb.filmservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class MovieDto {
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
