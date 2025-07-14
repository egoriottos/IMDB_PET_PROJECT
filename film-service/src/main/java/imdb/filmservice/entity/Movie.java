package imdb.filmservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rating;

    private String movie;

    private Integer year;

    private String country;

    @Column(name = "rating_ball")
    private Float ratingBall;

    @Column(columnDefinition = "TEXT")
    private String overview;

    private String director;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "screenwriter", columnDefinition = "text[]")
    private List<String> screenwriter;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "actors", columnDefinition = "text[]")
    private List<String> actors;

    @Column(name = "url_logo")
    private String urlLogo;
}
