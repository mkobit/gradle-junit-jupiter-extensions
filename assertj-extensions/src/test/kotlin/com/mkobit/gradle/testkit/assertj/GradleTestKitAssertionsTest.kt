package com.mkobit.gradle.testkit.assertj

import com.mkobit.gradle.testkit.assertj.GradleTestKitAssertions.assertThat
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.junit.jupiter.api.Test

internal class GradleTestKitAssertionsTest {
  @Test
  internal fun `can use static factory method for BuildTaskAssert`() {
    val mockBuildTask = mock<BuildTask>()
    assertThatCode { assertThat(mockBuildTask) }.doesNotThrowAnyException()
    assertThat(assertThat(mockBuildTask)).isExactlyInstanceOf(BuildTaskAssert::class.java)
  }

  @Test
  internal fun `can use static factory method for BuildResultAssert`() {
    val mockBuildResult = mock<BuildResult>()
    assertThatCode { assertThat(mockBuildResult) }.doesNotThrowAnyException()
    assertThat(assertThat(mockBuildResult)).isExactlyInstanceOf(BuildResultAssert::class.java)
  }
}
