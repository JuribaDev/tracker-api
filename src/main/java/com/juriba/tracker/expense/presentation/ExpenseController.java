package com.juriba.tracker.expense.presentation;

import com.juriba.tracker.expense.application.CreateExpenseUseCase;
import com.juriba.tracker.expense.application.GetExpenseUseCase;
import com.juriba.tracker.expense.application.GetListExpensesUseCase;
import com.juriba.tracker.expense.application.UpdateExpenseUseCase;
import com.juriba.tracker.expense.application.imp.CreateExpenseUseCaseImp;
import com.juriba.tracker.expense.presentation.dto.ExpenseRequest;
import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import com.juriba.tracker.expense.presentation.dto.UpdateExpenseRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/expenses")
@Tag(name = "Expense Endpoints")
public class ExpenseController {
    private final GetListExpensesUseCase getListExpensesUseCase;
    private final GetExpenseUseCase getExpenseUseCase;
    private final CreateExpenseUseCase createExpenseUseCase;
    private final UpdateExpenseUseCase updateExpenseUseCase;
    private final PagedResourcesAssembler<ExpenseResponse> pagedResourcesAssembler;



    public ExpenseController(GetListExpensesUseCase getListExpensesUseCase, GetExpenseUseCase getExpenseUseCase, CreateExpenseUseCaseImp createExpenseUseCase, UpdateExpenseUseCase updateExpenseUseCase, PagedResourcesAssembler<ExpenseResponse> pagedResourcesAssembler) {
        this.getListExpensesUseCase = getListExpensesUseCase;
        this.getExpenseUseCase = getExpenseUseCase;
        this.createExpenseUseCase = createExpenseUseCase;
        this.updateExpenseUseCase = updateExpenseUseCase;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<ExpenseResponse>>> getListExpenses(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Page<ExpenseResponse> responses = getListExpensesUseCase.execute(startDate, endDate, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(responses));
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@Validated @RequestBody ExpenseRequest request) {
        ExpenseResponse response = createExpenseUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable String id, @Validated @RequestBody UpdateExpenseRequest request) {
        ExpenseResponse response = updateExpenseUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpense( @PathVariable String id) {
        ExpenseResponse response = getExpenseUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}