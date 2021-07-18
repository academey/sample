import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

object Plugins {
    const val idea = "idea"
    const val kotlin = "kotlin"
    const val kotlinJvm = "org.jetbrains.kotlin.jvm"
    const val kotlinKapt = "org.jetbrains.kotlin.kapt"
    const val kotlinJpa = "org.jetbrains.kotlin.plugin.jpa"
    const val kotlinSpring = "org.jetbrains.kotlin.plugin.spring"
    const val kotlinAllopen = "org.jetbrains.kotlin.plugin.allopen"
    const val gitVersion = "com.palantir.git-version"
    const val ktLint = "org.jlleitschuh.gradle.ktlint"
    const val dependencyUpdates = "com.github.ben-manes.versions"
    const val dockerRemoteApi = "com.bmuschko.docker-remote-api"
    const val dockerCompose = "com.avast.gradle.docker-compose"
    const val springBoot = "org.springframework.boot"
    const val springDependencyManagement = "io.spring.dependency-management"
    const val jacoco = "jacoco"
}

fun PluginDependenciesSpec.useKotlin(withVersion: Boolean = false) {
    Plugins.idea
    id(Plugins.ktLint).withVersion(withVersion, Ver.ktLint)
    id(Plugins.kotlinJvm).withVersion(withVersion, Ver.kotlin)
    id(Plugins.kotlinKapt).withVersion(withVersion, Ver.kotlin)
    id(Plugins.kotlinAllopen).withVersion(withVersion, Ver.kotlin)
}

fun PluginDependenciesSpec.useSpring(withVersion: Boolean = false) {
    id(Plugins.kotlinJpa).withVersion(withVersion, Ver.kotlin)
    id(Plugins.kotlinSpring).withVersion(withVersion, Ver.kotlin)
    id(Plugins.springBoot).withVersion(withVersion, Ver.springBoot)
    id(Plugins.springDependencyManagement).withVersion(withVersion, Ver.springDependencyManagement)
}

fun PluginDependenciesSpec.useDocker(withVersion: Boolean = false) {
    id(Plugins.dockerCompose).withVersion(withVersion, Ver.dockerCompose)
    id(Plugins.dockerRemoteApi).withVersion(withVersion, Ver.dockerRemoteApi)
}

fun PluginDependenciesSpec.useGitVersion(withVersion: Boolean = false) {
    id(Plugins.gitVersion).withVersion(withVersion, Ver.gitVersion)
}

fun PluginDependenciesSpec.useDependencyUpdates(withVersion: Boolean = false) {
    id(Plugins.dependencyUpdates).withVersion(withVersion, Ver.dependencyUpdates)
}

fun PluginDependenciesSpec.useJacoco(withVersion: Boolean = false) {
    id(Plugins.jacoco).withVersion(withVersion, Ver.jacoco)
}

fun ExtensionAware.gitVersion() = run {
    val gitVersion: groovy.lang.Closure<String> by extra
    gitVersion
}

private fun PluginDependencySpec.withVersion(enabled: Boolean, version: String) =
    when (enabled) {
        true -> this.version(version)
        else -> this
    }
