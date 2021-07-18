// package com.example.upsideapi.repositories.custom
//
// import com.example.upsideapi.domain.history.QMatchHistory
// import com.example.upsideapi.domain.history.QMatchHistory.matchHistory
// import com.example.upsideapi.domain.match.MatchComment
// import com.example.upsideapi.utils.QuerydslUtils.paged
// import org.springframework.data.domain.Page
// import org.springframework.data.domain.Pageable
// import com.querydsl.jpa.hibernate.HibernateQueryFactory
// import org.hibernate.Session
// import org.springframework.data.domain.PageImpl
// import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
//
// interface MatchHistoryRepositoryCustom {
//     fun findAll(pageable: Pageable): Page<MatchComment>
//     fun findByMatch_Id(matchId: Long, pageable: Pageable): Page<MatchComment>
//     fun findByAnonymousUser_IdGroupByMatch(anonymousUserId: Long, pageable: Pageable): Page<Long>
//     fun findByUser_IdGroupByMatch(userId: Long, pageable: Pageable): Page<Long>
//     fun findUniqueWinnerPetByUser_Id(userId: Long, pageable: Pageable): List<Long>
//     fun findUniqueWinnerPetByAnonymousUser_Id(anonymousUserId: Long, pageable: Pageable): List<Long>
// }
//
// class MatchHistoryRepositoryCustomImpl : MatchHistoryRepositoryCustom, QuerydslRepositorySupport(QMatchHistory::class.java) {
//     private val queryFactory = HibernateQueryFactory { entityManager?.unwrap(Session::class.java) }
//
//     override fun findByAnonymousUser_IdGroupByMatch(anonymousUserId: Long, pageable: Pageable): Page<Long> =
//         queryFactory
//             .selectFrom(matchHistory)
//             .select(matchHistory.match.id)
//             .groupBy(matchHistory.match.id)
//             .where(matchHistory.anonymousUser.id.eq(anonymousUserId))
//             .paged(pageable)
//             .fetch()
//             .let { list ->
//                 @Suppress("UNCHECKED_CAST")
//                 PageImpl(
//                     list,
//                     pageable,
//                     list.count().toLong()
//                 )
//             }
//
//     override fun findByUser_IdGroupByMatch(userId: Long, pageable: Pageable): Page<Long> =
//         queryFactory
//             .selectFrom(matchHistory)
//             .select(matchHistory.match.id)
//             .where(matchHistory.user.id.eq(userId))
//             .groupBy(matchHistory.match.id)
//             .paged(pageable)
//             .fetch()
//             .let { list ->
//                 @Suppress("UNCHECKED_CAST")
//                 PageImpl(
//                     list,
//                     pageable,
//                     list.count().toLong()
//                 )
//             }
//
//     override fun findUniqueWinnerPetByUser_Id(userId: Long, pageable: Pageable): List<Long> =
//         queryFactory
//             .selectFrom(matchHistory)
//             .select(matchHistory.winnerPetPhoto.id)
//             .where(matchHistory.user.id.eq(userId))
//             .groupBy(matchHistory.winnerPetPhoto.id)
//             .paged(pageable)
//             .fetch()
//
//     override fun findUniqueWinnerPetByAnonymousUser_Id(anonymousUserId: Long, pageable: Pageable): List<Long> =
//         queryFactory
//             .selectFrom(matchHistory)
//             .select(matchHistory.winnerPetPhoto.id)
//             .where(matchHistory.anonymousUser.id.eq(anonymousUserId))
//             .groupBy(matchHistory.winnerPetPhoto.id)
//             .fetch()
//
//     // override fun findByUser_IdGroupByMatch(userId: Long, pageable: Pageable): Page<MatchHistory> =
//     //     queryFactory.
//     //     selectFrom(matchHistory)
//     //         .select(matchHistory)
//     //         .where(matchHistory.match.id.`in`(
//     //             JPAExpressions
//     //                 .select(matchHistory.match.id)
//     //                 .from(matchHistory)
//     //                 .groupBy(matchHistory.match.id)
//     //                 .where(matchHistory.user.id.eq(userId)
//     //                 ))
//     //         )
//     //         .paged(pageable)
//     //         .fetch()
//     //         .let {
//     //             @Suppress("UNCHECKED_CAST")
//     //             PageImpl(
//     //                 it,
//     //                 pageable,
//     //                 it.count().toLong()
//     //             )
//     //         }
// }
