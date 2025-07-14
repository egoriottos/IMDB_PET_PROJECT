package imdb.webservice.dto;

import lombok.Data;

@Data
public class PageableDto {
    private int pageNumber;
    private int pageSize;
    private SortDto sort;
    private int offset;
    private boolean paged;
    private boolean unpaged;
}
