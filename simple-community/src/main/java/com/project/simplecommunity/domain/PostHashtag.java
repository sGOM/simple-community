package com.project.simplecommunity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PostHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "postId")
    private Post post;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "hashtagId")
    private Hashtag hashtag;

    public PostHashtag of(Post post, Hashtag hashtag) {
        return new PostHashtag(post, hashtag);
    }

    private PostHashtag(Post post, Hashtag hashtag) {
        this.post = post;
        this.hashtag = hashtag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostHashtag that)) return false;
        return this.getId() != null && this.getId().equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
