package com.example.academey.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class ImmutableEntity(
    @CreatedDate
    @Column(nullable = false)
    @JsonIgnore
    var createdAt: LocalDateTime = LocalDateTime.now()
)
