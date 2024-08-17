package com.juriba.tracker.expense.presentation.mapper;

import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.presentation.CategoryController;
import com.juriba.tracker.expense.presentation.dto.CategoryResponse;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class CategoryMapper {
    public static CategoryResponse toCategoryResponse(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse(
                category.getId(),
                category.getName(),
                category.isDefault()
        );
        categoryResponse.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(CategoryController.class)
                                .getCategory(category.getId()))
                .withSelfRel());
        return categoryResponse;
    }
}
