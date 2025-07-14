package imdb.webservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDocDtoPage<T> {
    private List<MovieDocDto> content;       // Список фильмов
    private PageableDto pageable;         // Информация о пагинации
    private boolean last;                 // Это последняя страница?
    private int totalPages;               // Всего страниц
    private int totalElements;            // Всего элементов
    private int numberOfElements;         // Количество элементов на текущей странице
    private int size;                     // Размер страницы
    private int number;                   // Номер текущей страницы
    private SortDto sort;                 // Сортировка
    private boolean first;                // Это первая страница?
    private boolean empty;
}
