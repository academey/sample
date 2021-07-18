package com.example.academey.utils

import com.querydsl.jpa.JPQLQuery
import org.springframework.data.domain.Pageable

object QuerydslUtils {
    /** Applies [offset] and [limit] to the query. */
    inline fun <reified T> JPQLQuery<T>.paged(pageable: Pageable): JPQLQuery<T> = this
        .offset(pageable.offset)
        .limit(pageable.pageSize.toLong())
}
