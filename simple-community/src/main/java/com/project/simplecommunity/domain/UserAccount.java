package com.project.simplecommunity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserAccount extends AuditingDate {
    @Id
    @Column(length = 50)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    public UserAccount updateUserAccount(String password, String email) {

        Optional.ofNullable(password)
                .ifPresent(newPassword -> this.password = newPassword);

        Optional.ofNullable(email)
                .ifPresent(newEmail -> this.email = newEmail);

        return this;
    }

    private UserAccount(String userId, String password, String email) {
        this.userId = userId;
        this.password = password;
        this.email = email;
    }

    public static UserAccount of(String userId, String password, String email) {
        return new UserAccount(userId, password, email);
    }

//    @OneToMany
//    private List<Post> posts = new ArrayList<>();, 일단 post 에서 단방향하고 이후 필요하면 그때 양방향으로
//    @oneToMany
//    private List<Comment> comments = new ArrayList<>();, 위와 동일
//    @OneToMany(mappedBy = "subscriber")
//    private List<Subscription> subscriptionsAsSubscriber = new ArrayList<>();
//    @OneToMany(mappedBy = "publisher")
//    private List<Subscription> subscriptionsAsPublisher = new ArrayList<>();
}
