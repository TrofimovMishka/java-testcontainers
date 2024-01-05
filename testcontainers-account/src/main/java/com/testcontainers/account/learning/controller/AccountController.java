package com.testcontainers.account.learning.controller;

import com.testcontainers.account.learning.dto.AccountDto;
import com.testcontainers.account.learning.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

//    @PostMapping("/create/{userId}")
//    public ResponseEntity<AccountDto> createAccount(@PathVariable Long userId) {
//        AccountDto account = accountService.createAccount(userId);
//        return ResponseEntity.ok(account);
//    }
//
    @GetMapping("/{userId}")
    public ResponseEntity<AccountDto> getUserAccount(@PathVariable Long userId) {
        AccountDto account = accountService.findAccount(userId);
        return ResponseEntity.ok(account);
    }
}
