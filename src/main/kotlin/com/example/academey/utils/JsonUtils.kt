package com.example.academey.utils

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.OutputStream

internal object JsonUtils {
    internal val objectMapper = jacksonObjectMapper()
        .also {
            it.propertyNamingStrategy = PropertyNamingStrategy.SnakeCaseStrategy()
            it.findAndRegisterModules()
        }
}

internal fun <T> JsonUtils.toJson(obj: T?): String? = obj?.let(objectMapper::writeValueAsString)

internal fun <T> JsonUtils.nullSafeToJson(obj: T): String = obj.let(objectMapper::writeValueAsString)

internal fun JsonUtils.writeValue(obj: OutputStream, content: Any) = objectMapper.writeValue(obj, content)

internal inline fun <reified T> JsonUtils.fromJson(json: String) = objectMapper.readValue<T>(json)
