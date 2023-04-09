package com.project.simplecommunity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private UserAccount userAccount;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "postId")
    private Post post;

    public Likes of(UserAccount userAccount, Post post) {
        return new Likes(userAccount, post);
    }

    private Likes(UserAccount userAccount, Post post) {
        this.userAccount = userAccount;
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Likes likes)) return false;
        return id != null && id.equals(likes.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
