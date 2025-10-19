package vn.quocdk.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {
    private int page; // current page index (zero-based)
    private int pageSize; // number of items per page
    private int pages; // total number of pages
    private long total; // total number of elements/items
}
