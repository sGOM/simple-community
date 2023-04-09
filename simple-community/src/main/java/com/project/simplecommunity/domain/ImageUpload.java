package com.project.simplecommunity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ImageUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String imagePath;

    @Setter
    @ManyToOne(optional = false)
    private Post post;

    public ImageUpload of(String imagePath, Post post) {
        return new ImageUpload(imagePath, post);
    }

    private ImageUpload(String imagePath, Post post) {
        this.imagePath = imagePath;
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageUpload that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
