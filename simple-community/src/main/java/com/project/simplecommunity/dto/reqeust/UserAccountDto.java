package com.project.simplecommunity.dto.reqeust;

public record UserAccountDto(
        String userId,
        String password,
        String email
) {

    static public UserAccountDto of(String userId, String password, String email) {
        return new UserAccountDto(userId, password, email);
    }
}
