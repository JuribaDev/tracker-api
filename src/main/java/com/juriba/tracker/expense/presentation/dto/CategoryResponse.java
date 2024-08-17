package com.juriba.tracker.expense.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
public final class CategoryResponse extends RepresentationModel<CategoryResponse> {
    private final String id;
    private final String name;
    @JsonProperty("is_default")
    private final boolean isDefault;
}
