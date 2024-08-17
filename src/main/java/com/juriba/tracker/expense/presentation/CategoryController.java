package com.juriba.tracker.expense.presentation;

import com.juriba.tracker.common.presentation.dto.CommonSuccessResponse;
import com.juriba.tracker.expense.application.*;
import com.juriba.tracker.expense.presentation.dto.CategoryRequest;
import com.juriba.tracker.expense.presentation.dto.CategoryResponse;
import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@RequestMapping("api/v1/categories")
@Tag(name = "Category Endpoints")
public class CategoryController {
    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryUseCase getCategoryUseCase;
    private final GetListCategoriesUseCase getListCategoriesUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final PagedResourcesAssembler<CategoryResponse > pagedResourcesAssembler;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, GetCategoryUseCase getCategoryUseCase, GetListCategoriesUseCase getListCategoriesUseCase, UpdateCategoryUseCase updateCategoryUseCase, DeleteCategoryUseCase deleteCategoryUseCase, PagedResourcesAssembler<CategoryResponse> pagedResourcesAssembler) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoryUseCase = getCategoryUseCase;
        this.getListCategoriesUseCase = getListCategoriesUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Validated @RequestBody CategoryRequest request) {
        CategoryResponse response = createCategoryUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable String id) {
        CategoryResponse response = getCategoryUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<CategoryResponse>>> getListCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        var categories = getListCategoriesUseCase.execute(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(categories));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonSuccessResponse> deleteCategory(@PathVariable String id) {
        return ResponseEntity.ok(deleteCategoryUseCase.execute(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable String id, @Validated @RequestBody CategoryRequest request) {
        CategoryResponse response = updateCategoryUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

}
