package com.mkobit.gradle.testkit.assertj

import com.nhaarman.mockito_kotlin.atLeastOnce
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.junit.jupiter.api.Test
import org.mockito.verification.VerificationMode
import java.util.function.Consumer

internal class BuildResultAssertTest {

  @Test
  internal fun `constructed with null instance`() {
    val buildResultAssert = BuildResultAssert(null)
    assertThatThrownBy { buildResultAssert.isNotNull }
    assertThatCode { buildResultAssert.isNull() }.doesNotThrowAnyException()
  }

  @Test
  internal fun `output contains`() {
    val buildOutput = "this is the build output"
    val mockBuildResult: BuildResult = mock {
      on { output } doReturn buildOutput
    }
    val buildResultAssert = BuildResultAssert(mockBuildResult)

    assertThatThrownBy { buildResultAssert.outputContains("nope present") }.isInstanceOf(AssertionError::class.java)
    assertThatCode { buildResultAssert.outputContains("build output") }.doesNotThrowAnyException()
  }

  @Test
  internal fun `output does not contain`() {
    val buildOutput = "this is the build output"
    val mockBuildResult: BuildResult = mock {
      on { output } doReturn buildOutput
    }
    val buildResultAssert = BuildResultAssert(mockBuildResult)

    assertThatCode { buildResultAssert.outputDoesNotContain("nope present") }.doesNotThrowAnyException()
    assertThatThrownBy { buildResultAssert.outputDoesNotContain("build output") }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `output satisfies`() {
    val buildOutput = "this is the build output"
    val mockBuildResult: BuildResult = mock {
      on { output } doReturn buildOutput
    }
    val mockConsumer = mock<Consumer<String>>()
    val buildResultAssert = BuildResultAssert(mockBuildResult)

    assertThatCode { buildResultAssert.outputSatisfies(mockConsumer) }.doesNotThrowAnyException()

    verify(mockConsumer, times(1)).accept(buildOutput)
  }

  @Test
  internal fun `has task at path`() {
    val path = ":taskPath"
    val mockBuildTask: BuildTask = mock()
    val mockBuildResult: BuildResult = mock {
      on { task(path) } doReturn mockBuildTask
    }
    val buildResultAssert = BuildResultAssert(mockBuildResult)

    assertThatCode { buildResultAssert.hasTaskAtPath(":taskPath") }.doesNotThrowAnyException()
    verify(mockBuildResult, times(1)).task(path)
    assertThatThrownBy { buildResultAssert.hasTaskAtPath(":wrongPath") }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `does not have task at path`() {
    val path = ":taskPath"
    val mockBuildTask: BuildTask = mock()
    val mockBuildResult: BuildResult = mock {
      on { task(path) } doReturn mockBuildTask
    }
    val buildResultAssert = BuildResultAssert(mockBuildResult)

    assertThatThrownBy { buildResultAssert.doesNotHaveTaskAtPath(":taskPath") }.isInstanceOf(AssertionError::class.java)
    verify(mockBuildResult, times(1)).task(path)
    assertThatCode { buildResultAssert.doesNotHaveTaskAtPath(":wrongPath") }.doesNotThrowAnyException()
  }

  @Test
  internal fun `task at path satisfies`() {
    val path = ":taskPath"
    val mockBuildTask: BuildTask = mock()
    val mockBuildResult: BuildResult = mock {
      on { task(path) } doReturn mockBuildTask
    }
    val mockBuildTaskConsumer: Consumer<BuildTask?> = mock()
    val buildResultAssert = BuildResultAssert(mockBuildResult)

    assertThatCode {
      buildResultAssert.hasTaskAtPathSatisfying(":taskPath", mockBuildTaskConsumer)
    }.doesNotThrowAnyException()
    verify(mockBuildResult, times(1)).task(path)
    verify(mockBuildTaskConsumer, times(1)).accept(mockBuildTask)

    assertThatCode {
      buildResultAssert.hasTaskAtPathSatisfying(":noTask", mockBuildTaskConsumer)
    }.doesNotThrowAnyException()
    verify(mockBuildTaskConsumer, times(1)).accept(null)
  }
}
