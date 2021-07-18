package com.example.academey.domain.user

import com.example.academey.domain.BaseTimeEntity
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.ForeignKey
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@TypeDef(name = "pg_enum", typeClass = PostgreSQLEnumType::class)
@Table(name = "o_auth_provider", schema = "public")
class OAuthProvider(
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = ForeignKey(
            name = "fk_user"
        )
    )
    var user: User,
    @Column(columnDefinition = "provider", nullable = false)
    @Type(type = "pg_enum")
    @Enumerated(EnumType.STRING)
    val provider: SocialProvider,
    val uid: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) : BaseTimeEntity()
