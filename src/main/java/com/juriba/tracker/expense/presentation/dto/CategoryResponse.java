package com.juriba.tracker.expense.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryResponse(

        String id,


        String name,

        @JsonProperty("is_default")
        boolean isDefault
) {}
