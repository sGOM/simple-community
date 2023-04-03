package com.project.simplecommunity.Stub;

import com.project.simplecommunity.domain.UserAccount;
import com.project.simplecommunity.dto.reqeust.UserAccountPatchDto;
import com.project.simplecommunity.dto.reqeust.UserAccountPostDto;
import com.project.simplecommunity.dto.response.UserAccountResponseDto;

import java.util.List;
import java.util.stream.IntStream;

public class UserStub {
    public static UserAccount createUserAccount() {
        return UserAccount.of(
                "tester1",
                "pw1!@#$%",
                "tester1@mail.com"
        );
    }

    public static UserAccount createUserAccount(String userId, String password, String email) {
        return UserAccount.of(userId, password, email);
    }

    public static List<UserAccount> createUserAccountList(int userNum) {
        return IntStream.rangeClosed(1, userNum)
                        .mapToObj(i ->
                                createUserAccount(
                                        "tester" + i,
                                        "pw" + i + "!@#$%",
                                        "tester" + i + "@mail.com"
                                ))
                        .toList();
    }

    public static UserAccountPostDto createUserAccountPostDto() {
        return UserAccountPostDto.of(
                "tester1",
                "pw1!@#$%",
                "tester1@mail.com"
        );
    }

    public static UserAccountPatchDto createUserAccountPatchDto() {
        return UserAccountPatchDto.of(
                "pw1!@#$%modified",
                "tester1modified@mail.com"
        );
    }

    public static UserAccountResponseDto createUserAccountResponseDto() {
        return UserAccountResponseDto.from(createUserAccount());
    }
}
