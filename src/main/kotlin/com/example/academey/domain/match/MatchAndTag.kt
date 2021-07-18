package com.example.academey.domain.match

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.ManyToOne
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table
import javax.persistence.JoinColumn
@Entity
@Table(name = "match_and_tag", schema = "public")
@IdClass(MatchAndTagId::class)
class MatchAndTag(
    @Id
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "match_id")
    var match: Match,
    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id")
    var tag: Tag
)
