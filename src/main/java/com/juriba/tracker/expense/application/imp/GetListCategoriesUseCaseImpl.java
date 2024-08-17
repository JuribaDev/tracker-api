package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.expense.application.GetListCategoriesUseCase;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.presentation.dto.CategoryResponse;
import com.juriba.tracker.expense.presentation.mapper.CategoryMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class GetListCategoriesUseCaseImpl implements GetListCategoriesUseCase {
    private final CategoryRepository categoryRepository;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    public GetListCategoriesUseCaseImpl(CategoryRepository categoryRepository, GetAuthenticatedUserUseCase getAuthenticatedUserUseCase) {
        this.categoryRepository = categoryRepository;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> execute(int page, int size, String sortBy, String sortDirection) {
        String  userId = getAuthenticatedUserUseCase.execute().getId();
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Category> categories = categoryRepository.findAllByOwner_Id(userId,pageable);
        return categories.map(CategoryMapper::toCategoryResponse);
    }
}
