apply {
  plugin("org.jetbrains.kotlin.jvm")
  plugin("org.jetbrains.dokka")
}

val kotlinVersion by rootProject
val junitJupiterVersion: String by rootProject.extra

dependencies {
  api(kotlin("stdlib-jre8", kotlinVersion as String))
  api("org.junit.jupiter", "junit-jupiter-api", junitJupiterVersion)
}
