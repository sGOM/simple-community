package com.project.simplecommunity.dto.reqeust;

import com.project.simplecommunity.domain.UserAccount;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record UserAccountPostDto(
        @NotBlank(message = "아이디 입력은 필수입니다.")
        @Length(max = 30, message = "아이디 길이는 30자 를 넘으면 안됩니다.")
        String userId,
        @NotNull
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,20}$") //숫자, 알파벳, 특수문자(!@#$%^&*) 포함 8자 이상 20자 이하
        String password,
        @NotBlank(message = "이메일 입력은 필수입니다.")
        @Length(max = 30, message = "아이디 길이는 30자 를 넘으면 안됩니다.")
        @Email(message = "이메일 형식을 지켜야 합니다.")
        String email
) {

    static public UserAccountPostDto of(String userId, String password, String email) {
        return new UserAccountPostDto(userId, password, email);
    }

    public UserAccount toEntity() {
        return UserAccount.of(userId, password, email);
    }
}
