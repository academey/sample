package com.example.academey.utils.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

open class UpsideLogger {
    val log = getLogger()

    private fun getLogger(): Logger {
        return LoggerFactory.getLogger(unwrapCompanionClass(javaClass))
    }

    companion object {
        private fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
            return ofClass.enclosingClass?.takeIf {
                ofClass.enclosingClass.kotlin.companionObject?.java == ofClass
            } ?: ofClass
        }
    }
}
