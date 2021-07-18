package com.example.academey.repositories

import com.example.academey.domain.match.Tag
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface TagRepository : CrudRepository<Tag, Long> {
    @Query("SELECT id, name, created_at, updated_at from public.tag  where id in :ids", nativeQuery = true)
    fun findByTagIds(@Param("ids") tagIdList: HashSet<Any?>): HashSet<Tag>
}
