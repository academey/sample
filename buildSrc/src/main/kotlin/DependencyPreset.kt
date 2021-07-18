import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.springBoot(
    module: String
) = "org.springframework.boot:spring-boot-$module"

fun DependencyHandlerScope.jacksonModule(
    module: String
) = "com.fasterxml.jackson.module:jackson-module-$module"

fun DependencyHandlerScope.logback(
    module: String
) = "ch.qos.logback:logback-$module"

fun DependencyHandlerScope.janino(
) = "org.codehaus.janino:janino"

fun DependencyHandlerScope.logstashLogback(
    version: String = Ver.logstashLogback
) = "net.logstash.logback:logstash-logback-encoder:$version"

fun DependencyHandlerScope.postgres(
) = "org.postgresql:postgresql"

fun DependencyHandlerScope.queryDsl(
    module: String
) = "com.querydsl:querydsl-$module"

fun DependencyHandlerScope.flyway(
    module: String
) = "org.flywaydb:flyway-$module"

fun DependencyHandlerScope.jjwt(
    version: String = Ver.jjwt
) = "io.jsonwebtoken:jjwt:$version"

fun DependencyHandlerScope.hibernateTypes(
    version: String = Ver.hibernateTypes
) = "com.vladmihalcea:hibernate-types-52:$version"

fun DependencyHandlerScope.micrometer(
    module: String
) = "io.micrometer:micrometer-$module"

fun DependencyHandlerScope.jupiter(
    module: String
) = "org.junit.jupiter:$module"

fun DependencyHandlerScope.mockk(
    version: String = Ver.mockk
) = "io.mockk:mockk:$version"

fun DependencyHandlerScope.springMockk(
    version: String = Ver.springMockk
) = "com.ninja-squad:springmockk:$version"

fun DependencyHandlerScope.assertJ(
    module: String
) = "org.assertj:assertj-$module"

fun DependencyHandlerScope.jacksonCore(
) = "com.fasterxml.jackson.core:jackson-core"

fun DependencyHandlerScope.jacksonDatatype(
) = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"

fun DependencyHandlerScope.snakeYaml(
) = "org.yaml:snakeyaml"

fun DependencyHandlerScope.bouncyCastle(
    version: String = Ver.bouncyCastle
) = "org.bouncycastle:bcprov-jdk15to18:$version"
