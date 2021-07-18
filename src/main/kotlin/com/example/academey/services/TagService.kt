package com.example.academey.services

import com.example.academey.repositories.TagRepository
import org.springframework.stereotype.Service

@Service
class TagService(
    private val tagRepository: TagRepository
) {
    fun findAll() =
        tagRepository.findAll()
}
