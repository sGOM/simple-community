package com.project.simplecommunity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AuditingFields extends AuditingDate {
    @CreatedBy
    @Column(nullable = false, updatable = false, length = 100)
    protected String createdBy; // 생성자

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    protected String modifiedBy; // 수정자
}
