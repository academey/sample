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
@Table(name = "match_comment_like", schema = "public")
class MatchCommentLike(
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(
        name = "match_comment_id",
        foreignKey = ForeignKey(
            name = "fk_match_comment"
        )
    )
    var matchComment: MatchComment,
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], optional = true)
    @JoinColumn(
        name = "user_id",
        foreignKey = ForeignKey(
            name = "fk_user"
        )
    )
    var user: User? = null,
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], optional = true)
    @JoinColumn(
        name = "anonymous_user_id",
        foreignKey = ForeignKey(
            name = "fk_anonymous_user"
        )
    )
    var anonymousUser: AnonymousUser? = null,
    var isLiked: Boolean = true,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
) : BaseTimeEntity()
