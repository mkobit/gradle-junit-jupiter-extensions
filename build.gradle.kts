buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    // TODO: load from properties or script plugin
    classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.0-RC3")
  }
}

plugins {
  kotlin("jvm") apply false
  id("com.github.ben-manes.versions") version "0.15.0"
}

version = "0.1.0"
group = "com.mkobit.gradle.testkit"

repositories {
  jcenter()
}

tasks {
  "wrapper"(Wrapper::class) {
    gradleVersion = "4.1"
  }
}
