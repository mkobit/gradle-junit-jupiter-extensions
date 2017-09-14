import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.console.options.Details
import org.junit.platform.gradle.plugin.JUnitPlatformExtension

buildscript {
  repositories {
    mavenCentral()
    jcenter()
  }
  dependencies {
    // TODO: load from properties or script plugin
    classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.0")
    classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.9.15")
  }
}

plugins {
  kotlin("jvm") apply false
  id("com.github.ben-manes.versions") version "0.15.0"
}

tasks {
  "wrapper"(Wrapper::class) {
    gradleVersion = "4.1"
  }
}

allprojects {
  version = "0.1.0"
  group = "com.mkobit.gradle.testkit"
  repositories {
    jcenter()
    mavenCentral()
  }
}

apply {
  from("gradle/junit5.gradle.kts")
}

val kotlinVersion by project
val junitPlatformVersion: String by rootProject.extra
val junitTestImplementationArtifacts: Map<String, Map<String, String>> by rootProject.extra
val junitTestRuntimeOnlyArtifacts: Map<String, Map<String, String>> by rootProject.extra

subprojects {
  pluginManager.apply("java-library")
  pluginManager.apply("org.junit.platform.gradle.plugin")
  pluginManager.apply("org.jetbrains.kotlin.jvm")
  dependencies {
    "api"(gradleApi())
    "api"(gradleTestKit())
    "testImplementation"(kotlin("reflect", kotlinVersion as String))
    "testImplementation"("org.assertj:assertj-core:3.8.0")
    "testImplementation"("org.mockito:mockito-core:2.10.0")
    "testImplementation"("com.nhaarman:mockito-kotlin:1.5.0")
    junitTestImplementationArtifacts.values.forEach {
      "testImplementation"(it)
    }
    junitTestRuntimeOnlyArtifacts.values.forEach {
      "testRuntimeOnly"(it)
    }
    "testImplementation"(kotlin("stdlib-jre8", kotlinVersion as String))
  }

  extensions.getByType(JUnitPlatformExtension::class.java).apply {
    platformVersion = junitPlatformVersion
    filters {
      engines {
        include("junit-jupiter")
      }
    }
    logManager = "org.apache.logging.log4j.jul.LogManager"
    details = Details.TREE
  }

  configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  tasks.withType(KotlinCompile::class.java) {
    kotlinOptions.jvmTarget = "1.8"
  }
}
