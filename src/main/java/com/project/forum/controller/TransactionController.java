package com.project.forum.controller;

import com.project.forum.dto.responses.transaction.TransactionResponse;
import com.project.forum.dto.responses.transaction.TopAmountPostResponse;
import com.project.forum.exception.ApiResponse;
import com.project.forum.service.ITransactionService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/transaction")
@Tag(name = "15. Transaction")
public class TransactionController {

    ITransactionService transactionService;

    @SecurityRequirement(name = "BearerAuth")
    @GetMapping
    ResponseEntity<ApiResponse<Page<TransactionResponse>>> getALl(@RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "0") Integer size) {
        return ResponseEntity.ok(ApiResponse.<Page<TransactionResponse>>builder()
                .data(transactionService.getAllTransactions(page, size))
                .build());
    }
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/user")
    ResponseEntity<ApiResponse<Page<TransactionResponse>>> getAllByUser(@RequestParam(defaultValue = "0") Integer page,
                                                                        @RequestParam(defaultValue = "0") Integer size) {
        return ResponseEntity.ok(ApiResponse.<Page<TransactionResponse>>builder()
                .data(transactionService.getTransactionUser(page, size))
                .build());
    }

    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<TransactionResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<TransactionResponse>builder()
                .data(transactionService.getTransaction(id))
                .build());
    }


    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/{id}/ads")
    ResponseEntity<ApiResponse<TransactionResponse>> getByAdsId(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<TransactionResponse>builder()
                .data(transactionService.getTransactionByPayable_Id(id))
                .build());
    }

}
