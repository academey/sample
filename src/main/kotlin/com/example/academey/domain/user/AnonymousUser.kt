package com.example.academey.domain.user

import com.example.academey.domain.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "anonymous_user", schema = "public")
class AnonymousUser(
    @Column(unique = true)
    var uid: String,
    @Column(unique = true)
    var name: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
) : BaseTimeEntity() {
    fun update(name: String): AnonymousUser {
        this.name = name
        print("==============" + this.name)
        return this
    }
}
