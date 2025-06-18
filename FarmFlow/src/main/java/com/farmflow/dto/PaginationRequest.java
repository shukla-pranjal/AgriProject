package com.farmflow.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "Request object for pagination and sorting")
public class PaginationRequest {

    @Schema(description = "Page number (0-based)", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            minimum = "0")
    private int page = 0;

    @Schema(description = "Number of items per page", example = "10", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            minimum = "1", maximum = "100") // Reasonable upper limit
    private int size = 10;

    @Schema(description = "Field to sort by", example = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            allowableValues = {"id", "name", "price", "createdAt"}) // Example fields, adjust based on your entities
    private String sortBy = "id";

    @Schema(description = "Sort direction (true for ascending, false for descending)", example = "true",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private boolean ascending = true;

    public Pageable toPageable() {
        Sort sort = ascending
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }
}