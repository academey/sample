package com.example.academey.domain.user

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "anonymous_user_name", schema = "public")
class AnonymousUserName(
    @Column(unique = true)
    var name: String,
    @Column
    var seq: Long = 0L,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
) {
    fun plusSeq(): AnonymousUserName {
        this.seq = this.seq + 1
        return this
    }
}
