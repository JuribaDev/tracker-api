package com.juriba.tracker.expense.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateExpenseRequest(
        @Size(max = 255, message = "Description must be at most 255 characters")
        String description,

        @Positive(message = "Amount must be positive")
        @Digits(integer = 10, fraction = 2, message = "Amount must have at most 10 digits and 2 decimal places")
        BigDecimal amount
) {}
