package com.mkobit.gradle.testkit.assertj

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class BuildResultAssertTest {

  private lateinit var mockBuildResult: BuildResult
  private lateinit var mockBuildTask: BuildTask
  private lateinit var buildResultAssert: BuildResultAssert

  @BeforeEach
  internal fun setUp() {
    mockBuildResult = mock()
    mockBuildTask = mock()
    buildResultAssert = BuildResultAssert(mockBuildResult)
  }

  @Test
  internal fun `constructed with null instance`() {
    val nullActualBuildResultAssert = BuildResultAssert(null)
    assertThatThrownBy { nullActualBuildResultAssert.isNotNull }.isInstanceOf(AssertionError::class.java)
    assertThatCode { nullActualBuildResultAssert.isNull() }.doesNotThrowAnyException()
  }

  @Test
  internal fun `output contains`() {
    val buildOutput = "this is the build output"
    whenever(mockBuildResult.output).thenReturn(buildOutput)

    assertThatThrownBy { buildResultAssert.outputContains("nope present") }.isInstanceOf(AssertionError::class.java)
    assertThatCode { buildResultAssert.outputContains("build output") }.doesNotThrowAnyException()
  }

  @Test
  internal fun `output does not contain`() {
    val buildOutput = "this is the build output"
    whenever(mockBuildResult.output).thenReturn(buildOutput)

    assertThatCode { buildResultAssert.outputDoesNotContain("nope present") }.doesNotThrowAnyException()
    assertThatThrownBy { buildResultAssert.outputDoesNotContain("build output") }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `output satisfies`() {
    val buildOutput = "this is the build output"
    whenever(mockBuildResult.output).thenReturn(buildOutput)
    val mockConsumer = mock<Consumer<String>>()

    assertThatCode { buildResultAssert.outputSatisfies(mockConsumer) }.doesNotThrowAnyException()

    verify(mockConsumer, times(1)).accept(buildOutput)
  }

  @Test
  internal fun `tasks with outcome satisfy`() {
    val buildTasks = listOf(mockBuildTask)
    val taskOutcome = TaskOutcome.SUCCESS
    whenever(mockBuildResult.tasks(taskOutcome)).thenReturn(buildTasks)
    val mockTasksConsumer: Consumer<List<BuildTask>> = mock()

    assertThatCode {
      buildResultAssert.tasksWithOutcomeSatisfy(taskOutcome, mockTasksConsumer)
    }.doesNotThrowAnyException()
    verify(mockBuildResult, times(1)).tasks(taskOutcome)
    verify(mockTasksConsumer, times(1)).accept(buildTasks)
  }

  @Test
  internal fun `task paths with outcome satisfy`() {
    val taskPaths = listOf(":path1")
    val taskOutcome = TaskOutcome.SUCCESS
    whenever(mockBuildResult.taskPaths(taskOutcome)).thenReturn(taskPaths)
    val mockTaskPathsConsumer: Consumer<List<String>> = mock()

    assertThatCode {
      buildResultAssert.taskPathsWithOutcomeSatisfy(taskOutcome, mockTaskPathsConsumer)
    }.doesNotThrowAnyException()
    verify(mockBuildResult, times(1)).taskPaths(taskOutcome)
    verify(mockTaskPathsConsumer, times(1)).accept(taskPaths)
  }

  @Test
  internal fun `tasks satisfy`() {
    val tasks = listOf(mockBuildTask)
    whenever(mockBuildResult.tasks).thenReturn(tasks)
    val mockTasksConsumer: Consumer<List<BuildTask>> = mock()

    assertThatCode {
      buildResultAssert.tasksSatisfy(mockTasksConsumer)
    }.doesNotThrowAnyException()
    verify(mockBuildResult, times(1)).tasks
    verify(mockTasksConsumer, times(1)).accept(tasks)
  }

  @Test
  internal fun `has task at path with failed task outcome`() {
    val taskPath = ":taskPath"
    whenever(mockBuildResult.task(taskPath)).thenReturn(mockBuildTask)
    whenever(mockBuildTask.outcome).thenReturn(TaskOutcome.FAILED)
    assertThatCode {
      buildResultAssert.hasTaskFailedAtPath(taskPath)
    }.doesNotThrowAnyException()

    whenever(mockBuildTask.outcome).thenReturn(TaskOutcome.SUCCESS)
    assertThatThrownBy {
      buildResultAssert.hasTaskFailedAtPath(taskPath)
    }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `has task at path with from cache outcome`() {
    val taskPath = ":taskPath"
    whenever(mockBuildResult.task(taskPath)).thenReturn(mockBuildTask)
    whenever(mockBuildTask.outcome).thenReturn(TaskOutcome.FROM_CACHE)
    assertThatCode {
      buildResultAssert.hasTaskFromCacheAtPath(taskPath)
    }.doesNotThrowAnyException()

    whenever(mockBuildTask.outcome).thenReturn(TaskOutcome.SUCCESS)
    assertThatThrownBy {
      buildResultAssert.hasTaskFromCacheAtPath(taskPath)
    }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `has task at path with no source outcome`() {
    val taskPath = ":taskPath"
    whenever(mockBuildResult.task(taskPath)).thenReturn(mockBuildTask)
    whenever(mockBuildTask.outcome).thenReturn(TaskOutcome.NO_SOURCE)
    assertThatCode {
      buildResultAssert.hasTaskNoSourceAtPath(taskPath)
    }.doesNotThrowAnyException()

    whenever(mockBuildTask.outcome).thenReturn(TaskOutcome.SUCCESS)
    assertThatThrownBy {
      buildResultAssert.hasTaskNoSourceAtPath(taskPath)
    }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `has task at path with skipped outcome`() {
    val taskPath = ":taskPath"
    whenever(mockBuildResult.task(taskPath)).thenReturn(mockBuildTask)
    whenever(mockBuildTask.outcome).thenReturn(TaskOutcome.SKIPPED)
    assertThatCode {
      buildResultAssert.hasTaskSkippedAtPath(taskPath)
    }.doesNotThrowAnyException()

    whenever(mockBuildTask.outcome).thenReturn(TaskOutcome.SUCCESS)
    assertThatThrownBy {
      buildResultAssert.hasTaskSkippedAtPath(taskPath)
    }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `has task at path with success outcome`() {
    val taskPath = ":taskPath"
    whenever(mockBuildResult.task(taskPath)).thenReturn(mockBuildTask)
    whenever(mockBuildTask.outcome).thenReturn(TaskOutcome.SUCCESS)
    assertThatCode {
      buildResultAssert.hasTaskSuccessAtPath(taskPath)
    }.doesNotThrowAnyException()

    whenever(mockBuildTask.outcome).thenReturn(TaskOutcome.FAILED)
    assertThatThrownBy {
      buildResultAssert.hasTaskSuccessAtPath(taskPath)
    }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `has task at path with up-to-date outcome`() {
    val taskPath = ":taskPath"
    whenever(mockBuildResult.task(taskPath)).thenReturn(mockBuildTask)
    whenever(mockBuildTask.outcome).thenReturn(TaskOutcome.UP_TO_DATE)
    assertThatCode {
      buildResultAssert.hasTaskUpToDateAtPath(taskPath)
    }.doesNotThrowAnyException()

    whenever(mockBuildTask.outcome).thenReturn(TaskOutcome.SUCCESS)
    assertThatThrownBy {
      buildResultAssert.hasTaskUpToDateAtPath(taskPath)
    }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `has task at path with user - specified outcome`() {
    val taskPath = ":taskPath"
    whenever(mockBuildResult.task(taskPath)).thenReturn(mockBuildTask)
    whenever(mockBuildTask.outcome).thenReturn(TaskOutcome.FAILED)
    assertThatCode {
      buildResultAssert.hasTaskAtPathWithOutcome(taskPath, TaskOutcome.FAILED)
    }.doesNotThrowAnyException()

    assertThatThrownBy {
      buildResultAssert.hasTaskAtPathWithOutcome(taskPath, TaskOutcome.SUCCESS)
    }.isInstanceOf(AssertionError::class.java)
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
    whenever(mockBuildResult.task(path)).thenReturn(mockBuildTask)
    val buildResultAssert = BuildResultAssert(mockBuildResult)

    assertThatThrownBy { buildResultAssert.doesNotHaveTaskAtPath(":taskPath") }.isInstanceOf(AssertionError::class.java)
    verify(mockBuildResult, times(1)).task(path)
    assertThatCode { buildResultAssert.doesNotHaveTaskAtPath(":wrongPath") }.doesNotThrowAnyException()
  }

  @Test
  internal fun `task at path satisfies`() {
    val path = ":taskPath"
    whenever(mockBuildResult.task(path)).thenReturn(mockBuildTask)
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
