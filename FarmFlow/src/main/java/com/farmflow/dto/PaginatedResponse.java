package com.farmflow.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;


import org.springframework.data.domain.Sort;


@Data
@Schema(description = "Response object for paginated data")
public class PaginatedResponse<T> {

    @ArraySchema(
            schema = @Schema(description = "List of items in the current page", requiredMode = Schema.RequiredMode.REQUIRED)
    )
    @Schema(description = "List of items in the current page", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<T> content;

    @Schema(description = "Pagination information", requiredMode = Schema.RequiredMode.REQUIRED)
    private PageableInfo pageable;

    @Schema(description = "Total number of pages", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private int totalPages;

    @Schema(description = "Total number of items across all pages", example = "50", requiredMode = Schema.RequiredMode.REQUIRED)
    private long totalElements;

    @Schema(description = "Indicates if this is the last page", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean last;

    @Schema(description = "Indicates if this is the first page", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean first;

    @Schema(description = "Number of items per page", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private int size;

    @Schema(description = "Current page number (0-based)", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private int number;

    @Schema(description = "Sorting information", requiredMode = Schema.RequiredMode.REQUIRED)
    private SortInfo sort;

    @Schema(description = "Number of items in the current page", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private int numberOfElements;

    @Schema(description = "Indicates if the page is empty", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
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

    // Nested PageableInfo and SortInfo classes as defined above
    @Data
    @Schema(description = "Information about the pagination settings")
    public static class PageableInfo {
        @Schema(description = "Current page number (0-based)", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        private int pageNumber;

        @Schema(description = "Number of items per page", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
        private int pageSize;

        @Schema(description = "Sorting information", requiredMode = Schema.RequiredMode.REQUIRED)
        private SortInfo sort;

        @Schema(description = "Offset of the current page", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        private int offset;

        @Schema(description = "Indicates if pagination is disabled", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
        private boolean unpaged;

        @Schema(description = "Indicates if pagination is enabled", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        private boolean paged;
    }

    @Data
    @Schema(description = "Information about the sorting applied to the page")
    public static class SortInfo {
        @Schema(description = "Indicates if the data is sorted", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        private boolean sorted;

        @Schema(description = "Indicates if the data is unsorted", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
        private boolean unsorted;

        @Schema(description = "Indicates if no sorting is applied", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
        private boolean empty;

        public SortInfo(Sort sort) {
            this.sorted = sort.isSorted();
            this.unsorted = sort.isUnsorted();
            this.empty = sort.isEmpty();
        }
    }
}