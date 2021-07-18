import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.kotlin

fun DependencyHandlerScope.useKotlin() {
    "implementation"(kotlin("stdlib-jdk8"))
    "implementation"(kotlin("reflect"))
}

fun DependencyHandlerScope.usePostgres(migration: Boolean) {
    "runtimeOnly"(postgres())
    if (migration) {
        "implementation"(flyway("core"))
    }
}

fun DependencyHandlerScope.useSpringBoot(component: String) {
    "implementation"(springBoot(component))
}

fun DependencyHandlerScope.useSpringBootTest(component: String, exclude: ExternalModuleDependency.() -> Unit = {}) {
    "testImplementation"(springBoot(component), exclude)
}

fun DependencyHandlerScope.useMonitoring() {
    "implementation"(micrometer("core"))
    "implementation"(micrometer("registry-prometheus"))
}

fun DependencyHandlerScope.useJwt() {
    "implementation"(jjwt())
}

fun DependencyHandlerScope.useJunit5() {
    "testImplementation"(jupiter("junit-jupiter"))
    "testImplementation"(jupiter("junit-jupiter-api"))
    "testImplementation"(assertJ("core"))
    "testRuntimeOnly"(jupiter("junit-jupiter-engine"))
    "testImplementation"(mockk())
}

fun DependencyHandlerScope.useJackson(moduleOnly: Boolean = false) {
    if (!moduleOnly) {
        "implementation"(jacksonCore())
        "implementation"(jacksonDatatype())
    }
    "implementation"(jacksonModule("kotlin"))
}

fun DependencyHandlerScope.useLogstashLogback() {
    "implementation"(logback("classic"))
    "implementation"(logstashLogback())
    "runtimeOnly"(janino())
}

fun DependencyHandlerScope.useBouncyCastle() {
    "implementation"(bouncyCastle())
}
