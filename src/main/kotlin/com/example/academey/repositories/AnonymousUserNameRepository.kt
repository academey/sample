package com.example.academey.repositories

import com.example.academey.domain.user.AnonymousUserName
import org.springframework.data.repository.CrudRepository

interface AnonymousUserNameRepository : CrudRepository<AnonymousUserName, Long>
