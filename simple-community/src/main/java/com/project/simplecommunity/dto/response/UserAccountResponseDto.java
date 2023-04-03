package com.project.simplecommunity.dto.response;

import com.project.simplecommunity.domain.UserAccount;

public record UserAccountResponseDto(
        String userId,
        String email
) {

    public static UserAccountResponseDto of(String userId, String email) {
        return new UserAccountResponseDto(userId, email);
    }

    public static UserAccountResponseDto from(UserAccount entity) {
        return UserAccountResponseDto.of(
            entity.getUserId(),
            entity.getEmail()
        );
    }
}
