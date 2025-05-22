package com.farmflow.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;


import org.springframework.data.domain.Sort;


@Data
public class PaginatedResponse<T> {

    private List<T> content;
    private PageableInfo pageable;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private boolean first;
    private int size;
    private int number;
    private SortInfo sort;
    private int numberOfElements;
    private boolean empty;

    public static <T> PaginatedResponse<T> fromPage(Page<T> page) {
        PaginatedResponse<T> response = new PaginatedResponse<>();
        response.setContent(page.getContent());

        PageableInfo pageableInfo = new PageableInfo();
        pageableInfo.setPageNumber(page.getNumber());
        pageableInfo.setPageSize(page.getSize());
        pageableInfo.setSort(new SortInfo(page.getSort()));
        pageableInfo.setOffset((int) page.getPageable().getOffset());
        pageableInfo.setPaged(page.getPageable().isPaged());
        pageableInfo.setUnpaged(page.getPageable().isUnpaged());

        response.setPageable(pageableInfo);
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setLast(page.isLast());
        response.setFirst(page.isFirst());
        response.setSize(page.getSize());
        response.setNumber(page.getNumber());
        response.setSort(new SortInfo(page.getSort()));
        response.setNumberOfElements(page.getNumberOfElements());
        response.setEmpty(page.isEmpty());

        return response;
    }

    @Data
    public static class PageableInfo {
        private int pageNumber;
        private int pageSize;
        private SortInfo sort;
        private int offset;
        private boolean unpaged;
        private boolean paged;
    }

    @Data
    public static class SortInfo {
        private boolean sorted;
        private boolean unsorted;
        private boolean empty;

        public SortInfo(Sort sort) {
            this.sorted = sort.isSorted();
            this.unsorted = sort.isUnsorted();
            this.empty = sort.isEmpty();
        }
    }
}
