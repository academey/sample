package com.example.academey.domain.match

import com.example.academey.domain.BaseTimeEntity
import com.example.academey.domain.user.AnonymousUser
import com.example.academey.domain.user.User
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.ForeignKey
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
@Table(name = "match_comment_report", schema = "public")
class MatchCommentReport(
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = ForeignKey(
            name = "fk_user"
        )
    )
    var user: User?,
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "anonymous_user_id",
        nullable = false,
        foreignKey = ForeignKey(
            name = "fk_anonymous_user"
        )
    )
    var anonymousUser: AnonymousUser?,
    @ManyToOne
    @JoinColumn(
        name = "match_comment_id",
        nullable = false,
        foreignKey = ForeignKey(
            name = "fk_match_comment"
        )
    )
    var matchComment: MatchComment,
    var reason: String? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
) : BaseTimeEntity()
