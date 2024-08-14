package com.juriba.tracker.expense.application.imp;


import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.expense.application.CreateExpenseUseCase;
import com.juriba.tracker.expense.domain.Expense;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.infrastructure.ExpenseRepository;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.presentation.dto.ExpenseRequest;
import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import com.juriba.tracker.expense.presentation.mapper.ExpenseMapper;
import com.juriba.tracker.user.domain.User;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class CreateExpenseUseCaseImp implements CreateExpenseUseCase {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    public CreateExpenseUseCaseImp(ExpenseRepository expenseRepository, CategoryRepository categoryRepository, GetAuthenticatedUserUseCase getAuthenticatedUserUseCase) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
    }

    @Override
    @Transactional
    public ExpenseResponse execute(ExpenseRequest request) {
        User user = getAuthenticatedUserUseCase.execute();
        Category category ;
        if (request.categoryId() == null) {
            category = categoryRepository.findByName("UnLinked")
                    .orElseGet(() -> {
                        Category newCategory = new Category("UnLinked", user, true);
                        return categoryRepository.save(newCategory);
                    });
        } else {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }

        Expense expense = new Expense(request.description(), request.amount(), category);
        Expense savedExpense = expenseRepository.save(expense);

        return ExpenseMapper.toExpenseResponse(savedExpense);
    }
}
