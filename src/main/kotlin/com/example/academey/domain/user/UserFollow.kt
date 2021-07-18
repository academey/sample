package com.example.academey.domain.user

import com.example.academey.domain.BaseTimeEntity
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
@Table(name = "user_follow", schema = "public")
class UserFollow(
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], optional = false)
    @JoinColumn(
        name = "user_id",
        foreignKey = ForeignKey(
            name = "fk_user"
        )
    )
    var user: User,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], optional = false)
    @JoinColumn(
        name = "followed_user_id",
        foreignKey = ForeignKey(
            name = "fk_user"
        )
    )
    var followedUser: User,
    var isPicked: Boolean = true,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
) : BaseTimeEntity()
