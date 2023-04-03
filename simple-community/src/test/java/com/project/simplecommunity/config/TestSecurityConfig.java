package com.project.simplecommunity.config;

import com.project.simplecommunity.service.UserAccountService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import static com.project.simplecommunity.Stub.UserStub.createUserAccountResponseDto;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private UserAccountService userAccountService;

    @BeforeTestMethod
    public void securitySetUp() {
        given(userAccountService.getUserAccount(anyString()))
                .willReturn(createUserAccountResponseDto());
    }
}
