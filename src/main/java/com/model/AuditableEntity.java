package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"created", "updated"},
        allowGetters = true
)
@Getter
@Setter
public abstract class AuditableEntity implements Serializable {

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime created;


    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updated;

}