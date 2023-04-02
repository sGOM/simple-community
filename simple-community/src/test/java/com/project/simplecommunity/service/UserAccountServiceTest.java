package com.project.simplecommunity.service;

import com.project.simplecommunity.domain.UserAccount;
import com.project.simplecommunity.dto.reqeust.UserAccountPatchDto;
import com.project.simplecommunity.dto.reqeust.UserAccountPostDto;
import com.project.simplecommunity.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static com.project.simplecommunity.Stub.UserStub.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 유저")
@ExtendWith(MockitoExtension.class) // Mockito 사용을 위함
class UserAccountServiceTest {
    @InjectMocks private UserAccountService sut;

    @Mock private UserAccountRepository userAccountRepository;

    @DisplayName("[CREATE, 성공] 새로운 유저 정보가 주어졌을 때, 새로운 계정 생성")
    @Test
    void givenUserAccountInfo_whenCreatingUserAccount_thenCreatUserAccount() {
        // Given
        UserAccountPostDto postDto = createUserAccountPostDto();

        given(userAccountRepository.save(any(UserAccount.class))).willReturn(createUserAccount());

        // When
        sut.createUserAccount(postDto);

        // Then
        then(userAccountRepository).should().save(any(UserAccount.class));
    }



    @DisplayName("[READ, 성공] 유저의 아이디가 주어졌을 때, 단일 유저를 반환")
    @Test
    void givenUserId_whenReadingUserAccount_thenReturnUserAccount() {
        // Given
        String userId = "tester1";
        given(userAccountRepository.findById(anyString())).willReturn(Optional.of(createUserAccount()));

        // When
        sut.getUserAccount(userId);

        // Then
        then(userAccountRepository).should().findById(anyString());
    }

    @DisplayName("[READ, 성공] 읽어올 페이지에 대한 정보가 주어졌을 때, 해당 페이지에 해당하는 유저들의 정보를 반환")
    @Test
    void givenPageInfo_whenReadingUserAccounts_thenReturnUserAccountPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        given(userAccountRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        sut.getAllUserAccount(pageable);

        // Then
        then(userAccountRepository).should().findAll(pageable);
    }

    @DisplayName("[UPDATE, 성공] 수정할 유저의 아이디와 정보가 주어졌을 때, 해당 유저의 정보를 수정")
    @Test
    void givenUserIdAndUpdateInfo_whenUpdatingUserAccount_thenUpdateUserAccount() {
        // Given
        String userId = "tester1";
        UserAccountPatchDto patchDto = createUserAccountPatchDto();
        UserAccount updatedUserAccount = createUserAccount().updateUserAccount(patchDto.password(), patchDto.email());
        given(userAccountRepository.findById(anyString())).willReturn(Optional.of(createUserAccount()));
        given(userAccountRepository.save(any(UserAccount.class))).willReturn(updatedUserAccount);

        // When
        sut.updateUserAccount(userId, patchDto);

        // Then
        then(userAccountRepository).should().findById(anyString());
        then(userAccountRepository).should().save(any(UserAccount.class));
    }

    @DisplayName("[DELETE, 성공] 삭제할 유저의 아이디가 주어졌을 때, 해당 유저를 삭제")
    @Test
    void givenUserId_whenDeletingUserAccount_thenDeleteUserAccount() {
        // Given
        String userId = "tester1";
        willDoNothing().given(userAccountRepository).deleteById(anyString());

        // When
        sut.deleteUserAccount(userId);

        // Then
        then(userAccountRepository).should().deleteById(anyString());
    }
}