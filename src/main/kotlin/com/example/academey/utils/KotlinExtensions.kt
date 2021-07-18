package com.example.academey.utils

import java.nio.charset.StandardCharsets
import java.time.Duration

/**
 * @throws [CustomExceptions.InvalidArgument] If [this] is empty.
 */
fun <T> Collection<T>.throwIfEmpty(message: String?) = this
    .ifEmpty { throw CustomExceptions.BadRequestException(message ?: "Collection must not be empty.") }

/**
 * @return [null] if [this] is empty. [this] otherwise.
 */
fun <T> Collection<T>.takeIfNotEmpty() = this
    .takeIf(Collection<*>::isNotEmpty)

fun ByteArray.safeDecode(): String =
    String(this, StandardCharsets.ISO_8859_1)

fun Duration.takeIfNonNegative(): Duration? =
    takeUnless(Duration::isNegative)

inline fun <reified T, reified R> T.runIf(condition: Boolean, crossinline block: T.() -> R): R =
    when {
        condition -> block(this)
        else -> this as R
    }

fun Long.takeIfNonNegative(): Long? =
    takeUnless { this < 0 }

fun Long.takeIfPositive(): Long? =
    takeIf { this > 0 }

fun String?.takeIfNotEmpty(): String? =
    takeUnless { isNullOrEmpty() }
