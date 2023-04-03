package com.project.simplecommunity.dto.reqeust;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record UserAccountPatchDto(
        @Size(min = 8, max = 20)
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,20}$") //숫자, 알파벳, 특수문자(!@#$%^&*) 포함 8자 이상 20자 이하
        String password,
        @Length(max = 30, message = "아이디 길이는 30자 를 넘으면 안됩니다.")
        @Email(message = "이메일 형식을 지켜야 합니다.")
        String email
) {
    static public UserAccountPatchDto of(String password, String email) {
        return new UserAccountPatchDto(password, email);
    }
}
