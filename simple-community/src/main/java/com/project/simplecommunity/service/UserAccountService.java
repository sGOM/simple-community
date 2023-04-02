package com.project.simplecommunity.service;

import com.project.simplecommunity.domain.UserAccount;
import com.project.simplecommunity.dto.reqeust.UserAccountPatchDto;
import com.project.simplecommunity.dto.reqeust.UserAccountPostDto;
import com.project.simplecommunity.dto.response.UserAccountResponseDto;
import com.project.simplecommunity.exception.custom.UserAccountException;
import com.project.simplecommunity.exception.custom.UserAccountExceptionType;
import com.project.simplecommunity.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountResponseDto createUserAccount(UserAccountPostDto postDto) {
        return UserAccountResponseDto.from(userAccountRepository.save(postDto.toEntity()));
    }

    public UserAccountResponseDto getUserAccount(String userId) {
        return userAccountRepository.findById(userId)
                .map(UserAccountResponseDto::from)
                .orElseThrow(() -> new UserAccountException(UserAccountExceptionType.USER_ACCOUNT_NOT_FOUND));
    }

    public Page<UserAccountResponseDto> getAllUserAccount(Pageable pageable) {
        return userAccountRepository.findAll(pageable)
                .map(UserAccountResponseDto::from);
    }

    public UserAccountResponseDto updateUserAccount(String userId, UserAccountPatchDto patchDto) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                        .orElseThrow(() -> new UserAccountException(UserAccountExceptionType.USER_ACCOUNT_NOT_FOUND));
        userAccount.updateUserAccount(patchDto.password(), patchDto.email());

        return UserAccountResponseDto.from(userAccountRepository.save(userAccount));
    }

    public void deleteUserAccount(String userId) {
        userAccountRepository.deleteById(userId);
    }
}
