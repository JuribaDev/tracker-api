package com.juriba.tracker.expense.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public final class ExpenseResponse extends RepresentationModel<ExpenseResponse> {
    private final String id;
    private final String description;
    private final BigDecimal amount;
    @JsonProperty("created_at")
    private final Instant createdAt;
    private final CategoryResponse category;
}
