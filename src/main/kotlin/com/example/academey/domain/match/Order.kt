package com.example.academey.domain.match

import com.example.academey.services.MatchService
import java.util.function.Function

internal enum class Order(
    private val orderType: String,
    private val expression: Function<MatchService, List<Match>>
) {
    NAME("name", Function<MatchService, List<Match>> { service: MatchService -> service.findAllMatchOrderByTitleDesc() }),
    DATE("date", Function<MatchService, List<Match>> { service: MatchService -> service.findAllMatchOrderByCreatedAtDesc() }),
    RANK("rank", Function<MatchService, List<Match>> { service: MatchService -> service.findAllMatchOrderByCreatedAtDesc() }),
    DEFAULT("", Function<MatchService, List<Match>> { service: MatchService -> service.findAllMatchOrderByCreatedAtDesc() });

    fun order(matchService: MatchService): List<Match> {
        return expression.apply(matchService)
    }
    companion object {
        fun findByOrderType(order: String): Order {
            values().forEach { o -> (o.getOrderType() == order).let { return o } }
            return DEFAULT
        }
    }
    fun getOrderType(): String = this.orderType
}
