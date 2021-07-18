package com.example.academey.domain.user

import com.example.academey.domain.BaseTimeEntity
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@TypeDef(name = "pg_enum", typeClass = PostgreSQLEnumType::class)
@Table(name = "user", schema = "public")
class User(
    @Column(length = 100, nullable = true, unique = true)
    var email: String?,
    @Column(length = 100, nullable = false)
    var name: String,
    var picture: String? = null,
    @Column(columnDefinition = "role", nullable = false)
    @Type(type = "pg_enum")
    @Enumerated(EnumType.STRING)
    var role: Role,
    @Embedded
    var password: Password,
    var pickCount: Long = 0,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
) : BaseTimeEntity(), UserDetails {
    fun update(name: String?, picture: String?): User {
        if (name != null) this.name = name
        if (picture != null) this.picture = picture
        return this
    }

    fun update(name: String?): User {
        if (name != null) this.name = name
        return this
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val auth = ArrayList<GrantedAuthority>()
        auth.add(SimpleGrantedAuthority("ROLE_${role.key}"))

        return auth
    }

    override fun getPassword(): String? = password.value

    override fun getUsername(): String = email ?: ""

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
