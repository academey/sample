import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.kotlin.dsl.testImplementation
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType

plugins {
    useKotlin(withVersion = true)
    useSpring(withVersion = true)
    useDocker(withVersion = true)
    useGitVersion(withVersion = true)
    useDependencyUpdates(withVersion = true)
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:Greenwich.RELEASE")
    }
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    useKotlin()
    useJackson(moduleOnly = true)
    useBouncyCastle()
    useMonitoring()
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    useSpringBoot("starter-webflux")
    useSpringBoot("configuration-processor")
    useSpringBoot("starter-actuator")
    useSpringBoot("starter-data-jpa")
    useSpringBoot("starter-security")
    useSpringBoot("starter-data-jpa")
    useSpringBoot("starter-web")
    useSpringBoot("starter-validation")
    useSpringBoot("starter-oauth2-client")
    useSpringBoot("starter-mustache")
    useSpringBoot("starter-actuator")
    useSpringBoot("starter-batch")

    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.1.RELEASE")

    useSpringBootTest("starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }

    useJwt()
    useLogstashLogback()
    usePostgres(migration = true)

    implementation(queryDsl("jpa"))
    implementation(hibernateTypes())

    kapt(queryDsl("apt:${Ver.querydsl}:jpa"))
    kapt(springBoot("configuration-processor"))

    testImplementation(springMockk())

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:2.2.6.RELEASE")

    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.1.RELEASE")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.apache.httpcomponents:httpclient:4.5.2")

    implementation("io.springfox:springfox-swagger2:2.6.1")
    implementation("io.springfox:springfox-swagger-ui:2.6.1")
    implementation("com.nimbusds:nimbus-jose-jwt:3.10")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.batch:spring-batch-test")

    implementation("io.github.microutils:kotlin-logging:${Ver.kotlinLogging}")

    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
    implementation("org.modelmapper:modelmapper:2.3.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

if (System.getenv("CI") == null) { // GitHub Action
    tasks.withType<Test> {
        dockerCompose.isRequiredBy(this)
    }

    dockerCompose {
        useComposeFiles = listOf("docker-compose.yml")
        startedServices = listOf("db")
    }
}
