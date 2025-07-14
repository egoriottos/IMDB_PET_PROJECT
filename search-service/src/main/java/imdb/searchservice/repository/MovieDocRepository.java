package imdb.searchservice.repository;

import imdb.searchservice.entity.MovieDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieDocRepository extends ElasticsearchRepository<MovieDoc,Long> {
    @Query("""
            {
              "bool": {
                "should": [
                  {
                    "multi_match": {
                      "query": "#{#query}",
                      "fields": ["movie^4", "overview^3"],
                      "type": "best_fields",
                      "operator": "or"
                    }
                  },
                  {
                    "match_phrase": {
                      "movie": {
                        "query": "#{#query}",
                        "boost": 3
                      }
                    }
                  },
                  {
                    "match_phrase": {
                      "overview": {
                        "query": "#{#query}",
                        "boost": 2
                      }
                    }
                  }
                ],
                "minimum_should_match": 1
              }
            }
            """)
    Page<MovieDoc> searchByQuery(String query, Pageable pageable);
    List<MovieDoc> findAllByOrderByRatingBallDesc();
}
