import org.gradle.api.internal.HasConvention
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
  `java-library`
  `maven-publish`
  kotlin("jvm")
  id("org.jetbrains.dokka") version "0.9.16"
  id("com.github.ben-manes.versions") version "0.17.0"
}

val SourceSet.kotlin: SourceDirectorySet
  get() = (this as HasConvention).convention.getPlugin(KotlinSourceSet::class.java).kotlin

tasks {
  "wrapper"(Wrapper::class) {
    gradleVersion = "4.6-rc-2"
  }
}

version = "0.1.0"
group = "com.mkobit.gradle.test"

repositories {
  jcenter()
  mavenCentral()
}

apply {
  from("gradle/junit5.gradle.kts")
}

val junitPlatformVersion: String by rootProject.extra
val junitJupiterVersion: String by rootProject.extra
val junitTestImplementationArtifacts: Map<String, Map<String, String>> by rootProject.extra
val junitTestRuntimeOnlyArtifacts: Map<String, Map<String, String>> by rootProject.extra

dependencies {
  api(gradleApi())
  api(gradleTestKit())
  api("org.junit.jupiter", "junit-jupiter-api", junitJupiterVersion)
  implementation(kotlin("stdlib-jdk8"))
  implementation("io.github.microutils:kotlin-logging:1.5.3")
  testImplementation(kotlin("reflect"))
  testImplementation("org.assertj:assertj-core:3.9.1")
  testImplementation("org.mockito:mockito-core:2.14.0")
  testImplementation("com.nhaarman:mockito-kotlin:1.5.0")
  junitTestImplementationArtifacts.values.forEach {
    testImplementation(it)
  }
  junitTestRuntimeOnlyArtifacts.values.forEach {
    testRuntimeOnly(it)
  }
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

val main = java.sourceSets["main"]!!
// No Java in main source set
main.java.setSrcDirs(emptyList<Any>())

tasks {
  "test"(Test::class) {
    useJUnitPlatform()
    systemProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager")
  }
}

val sourcesJar by tasks.creating(Jar::class) {
  classifier = "sources"
  from(main.allSource)
  description = "Assembles a JAR of the source code"
  group = JavaBasePlugin.DOCUMENTATION_GROUP
}

val dokka by tasks.getting(DokkaTask::class) {
  dependsOn(main.classesTaskName)
  outputFormat = "html"
  outputDirectory = "$buildDir/javadoc"
  sourceDirs = main.kotlin.srcDirs
}

val javadocJar by tasks.creating(Jar::class) {
  dependsOn(dokka)
  from(dokka.outputDirectory)
  classifier = "javadoc"
  description = "Assembles a JAR of the generated Javadoc"
  group = JavaBasePlugin.DOCUMENTATION_GROUP
}

tasks["assemble"].dependsOn(sourcesJar, javadocJar)

publishing {
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
