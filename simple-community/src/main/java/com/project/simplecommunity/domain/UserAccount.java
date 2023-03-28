package com.project.simplecommunity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserAccount {
    @Id
    @Column(length = 50)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

//    @OneToMany
//    private List<Post> posts = new ArrayList<>();, 일단 post 에서 단방향하고 이후 필요하면 그때 양방향으로
//    @oneToMany
//    private List<Comment> comments = new ArrayList<>();, 위와 동일
//    @OneToMany(mappedBy = "subscriber")
//    private List<Subscription> subscriptionsAsSubscriber = new ArrayList<>();
//    @OneToMany(mappedBy = "publisher")
//    private List<Subscription> subscriptionsAsPublisher = new ArrayList<>();
}
