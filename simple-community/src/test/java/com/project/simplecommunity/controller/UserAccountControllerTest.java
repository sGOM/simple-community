package com.project.simplecommunity.controller;

import com.project.simplecommunity.config.TestSecurityConfig;
import com.project.simplecommunity.dto.reqeust.UserAccountPatchDto;
import com.project.simplecommunity.dto.reqeust.UserAccountPostDto;
import com.project.simplecommunity.service.UserAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.project.simplecommunity.Stub.UserStub.createUserAccountResponseDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("컨트롤러 - 유저")
@Import(TestSecurityConfig.class)
@WebMvcTest(UserAccountController.class)
class UserAccountControllerTest {

    private final MockMvc mvc;
    private final UserAccountService userAccountService;

    public UserAccountControllerTest(
            @Autowired MockMvc mvc,
            @Autowired UserAccountService userAccountService
    ) {
        this.mvc = mvc;
        this.userAccountService = userAccountService;
    }

    @DisplayName("[POST, 성공] 유저 정보 그리고 유저 생성 요청을 받으면 유저를 생성한다")
    @Test
    void givenUserAccountInfo_whenRequesting_thenReturnNothing() throws Exception {
        // Given
        String requestBody = """
                {
                    "userId": "tester1",
                    "password": "pw1!@#$%",
                    "email": "tester1@mail.com"
                }
                """;
        given(userAccountService.createUserAccount(any(UserAccountPostDto.class))).willReturn(createUserAccountResponseDto());

        // When & Then
        mvc.perform(post("/user-account")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isCreated());

        then(userAccountService).should().createUserAccount(any(UserAccountPostDto.class));
    }

    @DisplayName("[GET, 성공] 유저 id 그리고 조회 요청을 받으면 해당 id에 해당하는 유저를 반환한다")
    @Test
    void givenUserId_whenRequesting_thenReturnUserAccount() throws Exception {
        // Given
        String userId = "tester1";
        given(userAccountService.getUserAccount(anyString())).willReturn(createUserAccountResponseDto());

        // When & Then
        mvc.perform(get("/user-account/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        then(userAccountService).should().getUserAccount(anyString());
    }

    @DisplayName("[GET, 성공] 페이지 정보 그리고 조회 요청을 받으면 유저 페이지를 반환한다" )
    @Test
    void givenPageInfo_whenRequesting_thenReturnUserAccountPage() throws Exception {
        // Given
        given(userAccountService.getAllUserAccount(any(Pageable.class))).willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/user-account")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        then(userAccountService).should().getAllUserAccount(any(Pageable.class));
    }

    @DisplayName("[UPDATE, 성공] 유저 id와 수정할 유저 정보 그리고 수정 요청을 받으면 유저 정보를 수정한다")
    @Test
    void givenUserIdAndUpdateInfo_whenRequesting_thenReturnNothing() throws Exception {
        // Given
        String userId = "tester1";
        String requestBody = """
                {
                    "password": "pw1!@#$%Modified",
                    "email": "tester1Modified@mail.com"
                }
                """;
        given(userAccountService.updateUserAccount(eq(userId), any(UserAccountPatchDto.class))).willReturn(createUserAccountResponseDto());

        // When & Then
        mvc.perform(patch("/user-account/{user-Id}", userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isOk());

        then(userAccountService).should().updateUserAccount(eq(userId), any(UserAccountPatchDto.class));
    }

    @DisplayName("[DELETE, 성공] 유저 id 그리고 삭제 요청을 받으면 유저 정보를 삭제한다")
    @Test
    void givenUserId_whenRequesting_thenReturnNothing() throws Exception {
        // Given
        String userId = "tester1";
        willDoNothing().given(userAccountService).deleteUserAccount(anyString());

        // When & Then
        mvc.perform(delete("/user-account/{user-Id}", userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

        then(userAccountService).should().deleteUserAccount(anyString());
    }
}