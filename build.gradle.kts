import org.gradle.api.internal.HasConvention
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.console.options.Details
import org.junit.platform.gradle.plugin.JUnitPlatformExtension
import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

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

val SourceSet.kotlin: SourceDirectorySet
  get() = (this as HasConvention).convention.getPlugin(KotlinSourceSet::class.java).kotlin

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

// TODO: come up with better way to configure Dokka only for the Kotlin subprojects
val kotlinSubprojects = setOf("kotlin-extensions")

subprojects {
  pluginManager.apply("java-library")
  pluginManager.apply("org.junit.platform.gradle.plugin")
  pluginManager.apply("org.jetbrains.kotlin.jvm")
  pluginManager.apply("maven-publish")
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

  val java = the<JavaPluginConvention>()

  java.apply {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  val main = java.sourceSets["main"]!!

  val sourcesJar by tasks.creating(Jar::class) {
    classifier = "sources"
    from(java.sourceSets["main"]!!.allSource)
    description = "Assembles a JAR of the source code"
    group = JavaBasePlugin.DOCUMENTATION_GROUP
  }

  val javadocJar by tasks.creating(Jar::class) {
    classifier = "javadoc"
    val javadoc by tasks.getting(Javadoc::class)
    dependsOn(javadoc)
    from(javadoc.destinationDir)
    description = "Assembles a JAR of the generated Javadoc"
    group = JavaBasePlugin.DOCUMENTATION_GROUP
  }

  pluginManager.withPlugin("org.jetbrains.dokka") {
    val dokka by tasks.getting(DokkaTask::class) {
      dependsOn(main.classesTaskName)
      outputFormat = "html"
      outputDirectory = "$buildDir/javadoc"
      sourceDirs = main.kotlin.srcDirs
    }

    javadocJar.apply {
      dependsOn(dokka)
      from(dokka.outputDirectory)
    }
  }

  tasks["assemble"].dependsOn(sourcesJar, javadocJar)

  extensions.getByType(PublishingExtension::class.java).apply {
    publications.invoke {
      "library"(MavenPublication::class) {
        from(components["java"])
        artifact(sourcesJar)
        artifact(javadocJar)
      }
    }
  }

  tasks.withType(KotlinCompile::class.java) {
    kotlinOptions.jvmTarget = "1.8"
  }
}
