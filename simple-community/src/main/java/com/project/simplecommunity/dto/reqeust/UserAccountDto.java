package com.project.simplecommunity.dto.reqeust;

import com.project.simplecommunity.domain.UserAccount;

public record UserAccountDto(
        String userId,
        String password,
        String email
) {

    static public UserAccountDto of(String userId, String password, String email) {
        return new UserAccountDto(userId, password, email);
    }

    public static UserAccountDto from(UserAccount entity) {
        return UserAccountDto.of(
                entity.getUserId(),
                entity.getPassword(),
                entity.getEmail()
        );
    }

    public UserAccount toEntity() {
        return UserAccount.of(userId, password, email);
    }

}
