package imdb.searchservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import java.util.List;

@Document(indexName = "movies_index")
@Setting(settingPath = "/elasticsearch-settings.json")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MovieDoc {
    @Id
    private Long id;

    @Field(type = FieldType.Integer)
    private Integer rating;

    @Field(type = FieldType.Text, analyzer = "russian_analyzer")
    private String movie;

    @Field(type = FieldType.Integer)
    private Integer year;

    @Field(type = FieldType.Keyword)
    private String country;

    @Field(name = "rating_ball", type = FieldType.Float)
    private Float ratingBall;

    @Field(type = FieldType.Text, analyzer = "russian_analyzer")
    private String overview;

    @Field(type = FieldType.Text)
    private String director;

    @Field(type = FieldType.Text)
    private List<String> screenwriter;

    @Field(type = FieldType.Text)
    private List<String> actors;

    @Field(name = "url_logo", type = FieldType.Keyword)
    private String urlLogo;
}
