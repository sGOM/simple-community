package com.project.simplecommunity.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(updatable = false)
    private Long parentCommentId; // 부모 댓글 ID

    //@OrderBy("createdAt ASC") 이후 auditing field 추가한 후에 적용
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL)
    private List<PostComment> childComments = new ArrayList<>();

    @ManyToOne(optional = false)
    private Post post;

    @ManyToOne(optional = false)
    private UserAccount userAccount;
}
