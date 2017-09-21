package com.mkobit.gradle.testkit.assertj

import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.junit.jupiter.api.Test

internal class GradleTestKitSoftAssertionsTest {
  @Test
  internal fun `can use soft assertions with BuildTask`() {
    val mockBuildTask: BuildTask = mock()

    Assertions.assertThatCode {
      GradleTestKitSoftAssertions().apply { assertThat(mockBuildTask).isNotNull }.assertAll()
    }.doesNotThrowAnyException()
    Assertions.assertThatThrownBy {
      GradleTestKitSoftAssertions().apply { assertThat(mockBuildTask).isNull() }.assertAll()
    }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `can use soft assertions with BuildResult`() {
    val mockBuildResult: BuildResult = mock()

    Assertions.assertThatCode {
      GradleTestKitSoftAssertions().apply { assertThat(mockBuildResult).isNotNull }
    }.doesNotThrowAnyException()
    Assertions.assertThatThrownBy {
      GradleTestKitSoftAssertions().apply { assertThat(mockBuildResult).isNull() }.assertAll()
    }.isInstanceOf(AssertionError::class.java)
  }
}
