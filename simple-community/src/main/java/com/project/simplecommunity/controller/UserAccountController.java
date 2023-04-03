package com.project.simplecommunity.controller;

import com.project.simplecommunity.dto.reqeust.UserAccountPatchDto;
import com.project.simplecommunity.dto.reqeust.UserAccountPostDto;
import com.project.simplecommunity.dto.response.UserAccountResponseDto;
import com.project.simplecommunity.dto.response.wrapper.PageResponseDto;
import com.project.simplecommunity.dto.response.wrapper.SingleResponseDto;
import com.project.simplecommunity.service.UserAccountService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user-account")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @PostMapping
    public ResponseEntity<SingleResponseDto<UserAccountResponseDto>> postUserAccount(
            @RequestBody UserAccountPostDto postDto
    ) {
        UserAccountResponseDto userAccount = userAccountService.createUserAccount(postDto);

        return new ResponseEntity<>(
                new SingleResponseDto<>(userAccount),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<SingleResponseDto<UserAccountResponseDto>> getUserAccount(
            @PathVariable("user-id") @NotBlank String userId
    ) {
        UserAccountResponseDto userAccount = userAccountService.getUserAccount(userId);

        return new ResponseEntity<>(
                new SingleResponseDto<>(userAccount),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<UserAccountResponseDto>> getUserAccounts(
            @PageableDefault(size = 10, page = 0, sort = "userId") Pageable pageable
    ) {
        Page<UserAccountResponseDto> userAccountPage = userAccountService.getAllUserAccount(pageable);

        return new ResponseEntity<>(
                new PageResponseDto<>(userAccountPage.getContent(), userAccountPage),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{user-id}")
    public ResponseEntity<SingleResponseDto<UserAccountResponseDto>> patchUserAccount(
            @PathVariable("user-id") @NotBlank String userId,
            @RequestBody UserAccountPatchDto patchDto
    ) {
        UserAccountResponseDto userAccount = userAccountService.updateUserAccount(userId, patchDto);

        return new ResponseEntity<>(
                new SingleResponseDto<>(userAccount),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity<Void> deleteUserAccount(
            @PathVariable("user-id") @NotBlank String userId
    ) {
        userAccountService.deleteUserAccount(userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
