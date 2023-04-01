package com.project.simplecommunity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
