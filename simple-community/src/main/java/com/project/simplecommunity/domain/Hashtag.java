package com.project.simplecommunity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Hashtag extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String hashtagName;

    @ToString.Exclude
    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL)
    private final Set<PostHashtag> postHashtags = new LinkedHashSet<>();

    public static Hashtag of(String hashtagName) {
        return new Hashtag(hashtagName);
    }

    private Hashtag(String hashtagName) {
        this.hashtagName = hashtagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hashtag hashtag)) return false;
        return this.getId() != null && this.getId().equals(hashtag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
