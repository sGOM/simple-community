package com.project.simplecommunity.repository;

import com.project.simplecommunity.config.TestJpaConfig;
import com.project.simplecommunity.domain.UserAccount;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.project.simplecommunity.Stub.UserStub.createUserAccountList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserAccount JPA 연결 테스트")
// https://stackoverflow.com/questions/52551718/what-use-is-testinstance-annotation-in-junit-5
// default 는 PER_METHOD
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(TestJpaConfig.class)
@DataJpaTest    // Spring Boot 가 자동으로 H2 DB를 설정하고 테스트 중 실행한 모든 변경 사항을 롤백하여 반복적인 테스트를 가능하게함
class UserAccountRepositoryTest {

    private final UserAccountRepository userAccountRepository;

    UserAccountRepositoryTest(@Autowired UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @BeforeAll
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = userAccountRepository.count();
        List<UserAccount> userAccounts = createUserAccountList(3);

        // When
        userAccountRepository.saveAll(userAccounts);

        // Then
        assertThat(userAccountRepository.count()).isEqualTo(previousCount + userAccounts.size());
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<UserAccount> userAccounts = userAccountRepository.findAll();

        // Then
        assertThat(userAccounts)
                .isNotNull()
                .hasSize(3); // 위에 @BeforeAll 참조
    }


    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // Given
        UserAccount userAccount = userAccountRepository.findById("tester1").orElseThrow();
        userAccount.updateUserAccount(null, "testerModified@mail.com");

        // When
        UserAccount savedUserAccount = userAccountRepository.saveAndFlush(userAccount);

        // Then
        assertThat(savedUserAccount.getEmail())
                .isEqualTo("testerModified@mail.com");
    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        UserAccount userAccount = userAccountRepository.findById("tester1").orElseThrow();
        long previousArticleCount = userAccountRepository.count();

        // When
        userAccountRepository.delete(userAccount);

        // Then
        assertThat(userAccountRepository.count()).isEqualTo(previousArticleCount - 1);
    }
}
