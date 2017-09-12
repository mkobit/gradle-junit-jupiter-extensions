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

    Assertions.assertThatCode { GradleTestKitAssertions.assertThat(mockBuildTask).isNotNull }.doesNotThrowAnyException()
    Assertions.assertThatThrownBy { GradleTestKitAssertions.assertThat(mockBuildTask).isNull() }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `can use soft assertions with BuildResult`() {
    val mockBuildResult: BuildResult = mock()

    Assertions.assertThatCode { GradleTestKitAssertions.assertThat(mockBuildResult).isNotNull }.doesNotThrowAnyException()
    Assertions.assertThatThrownBy { GradleTestKitAssertions.assertThat(mockBuildResult).isNull() }.isInstanceOf(AssertionError::class.java)
  }
}
