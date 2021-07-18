package com.example.academey.domain.match

import com.example.academey.domain.BaseTimeEntity
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Column
import javax.persistence.OneToMany
@Entity
@Table(name = "tag", schema = "public")
class Tag(
    @Column(length = 100, nullable = true, unique = true)
    var name: String,
    @OneToMany(mappedBy = "tag")
    @JsonIgnore
    var matchAndTags: MutableSet<MatchAndTag>,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) : BaseTimeEntity()
