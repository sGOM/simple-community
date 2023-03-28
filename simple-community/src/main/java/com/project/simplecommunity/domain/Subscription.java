package com.project.simplecommunity.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Subscription {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subscriber_id")
    private UserAccount subscriber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "publisher_id")
    private UserAccount publisher;
}
