package com.mkobit.gradle.testkit.assertj

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test
import java.util.function.Consumer
import java.util.regex.Pattern

internal class BuildTaskAssertTest {
  companion object {
    private val TEST_PATH = ":taskName"
  }

  @Test
  internal fun `null constructor argument`() {
    val buildTaskAssert = BuildTaskAssert(null)
    assertThatCode { buildTaskAssert.isNull() }.doesNotThrowAnyException()
    assertThatThrownBy { buildTaskAssert.isNotNull }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `FAILED task outcome`() {
    val buildTask: BuildTask = mock {
      on { outcome } doReturn TaskOutcome.FAILED
      on { path } doReturn TEST_PATH
    }
    val buildTaskAssert = BuildTaskAssert(buildTask)

    assertThatCode { buildTaskAssert.hasTaskOutcome(TaskOutcome.FAILED) }

    assertThatCode { buildTaskAssert.isFailed }.doesNotThrowAnyException()
    assertThatThrownBy { buildTaskAssert.isFromCache }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isNoSource }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isSkipped }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isSuccess }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isUpToDate }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `FROM_CACHE task outcome`() {
    val buildTask: BuildTask = mock {
      on { outcome } doReturn TaskOutcome.FROM_CACHE
      on { path } doReturn TEST_PATH
    }
    val buildTaskAssert = BuildTaskAssert(buildTask)

    assertThatCode { buildTaskAssert.hasTaskOutcome(TaskOutcome.FROM_CACHE) }

    assertThatThrownBy { buildTaskAssert.isFailed }.isInstanceOf(AssertionError::class.java)
    assertThatCode { buildTaskAssert.isFromCache }.doesNotThrowAnyException()
    assertThatThrownBy { buildTaskAssert.isNoSource }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isSkipped }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isSuccess }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isUpToDate }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `NO_SOURCE task outcome`() {
    val buildTask: BuildTask = mock {
      on { outcome } doReturn TaskOutcome.NO_SOURCE
      on { path } doReturn TEST_PATH
    }
    val buildTaskAssert = BuildTaskAssert(buildTask)

    assertThatCode { buildTaskAssert.hasTaskOutcome(TaskOutcome.NO_SOURCE) }

    assertThatThrownBy { buildTaskAssert.isFailed }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isFromCache }.isInstanceOf(AssertionError::class.java)
    assertThatCode { buildTaskAssert.isNoSource }.doesNotThrowAnyException()
    assertThatThrownBy { buildTaskAssert.isSkipped }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isSuccess }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isUpToDate }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `SKIPPED task outcome`() {
    val buildTask: BuildTask = mock {
      on { outcome } doReturn TaskOutcome.SKIPPED
      on { path } doReturn TEST_PATH
    }
    val buildTaskAssert = BuildTaskAssert(buildTask)

    assertThatCode { buildTaskAssert.hasTaskOutcome(TaskOutcome.SKIPPED) }

    assertThatThrownBy { buildTaskAssert.isFromCache }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isNoSource }.isInstanceOf(AssertionError::class.java)
    assertThatCode { buildTaskAssert.isSkipped }.doesNotThrowAnyException()
    assertThatThrownBy { buildTaskAssert.isSuccess }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isUpToDate }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `SUCCESS task outcome`() {
    val buildTask: BuildTask = mock {
      on { outcome } doReturn TaskOutcome.SUCCESS
      on { path } doReturn TEST_PATH
    }
    val buildTaskAssert = BuildTaskAssert(buildTask)

    assertThatCode { buildTaskAssert.hasTaskOutcome(TaskOutcome.SUCCESS) }

    assertThatThrownBy { buildTaskAssert.isFailed }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isFromCache }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isNoSource }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isSkipped }.isInstanceOf(AssertionError::class.java)
    assertThatCode { buildTaskAssert.isSuccess }.doesNotThrowAnyException()
    assertThatThrownBy { buildTaskAssert.isUpToDate }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `UP_TO_DATE task outcome`() {
    val buildTask: BuildTask = mock {
      on { outcome } doReturn TaskOutcome.UP_TO_DATE
      on { path } doReturn TEST_PATH
    }
    val buildTaskAssert = BuildTaskAssert(buildTask)

    assertThatCode { buildTaskAssert.hasTaskOutcome(TaskOutcome.UP_TO_DATE) }

    assertThatThrownBy { buildTaskAssert.isFailed }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isFromCache }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isNoSource }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isSkipped }.isInstanceOf(AssertionError::class.java)
    assertThatThrownBy { buildTaskAssert.isSuccess }.isInstanceOf(AssertionError::class.java)
    assertThatCode { buildTaskAssert.isUpToDate }.doesNotThrowAnyException()
  }

  @Test
  internal fun `path equals`() {
    val buildTask: BuildTask = mock {
      on { path } doReturn TEST_PATH
    }
    val buildTaskAssert = BuildTaskAssert(buildTask)

    assertThatCode { buildTaskAssert.pathIsEqualTo(TEST_PATH) }.doesNotThrowAnyException()
    assertThatThrownBy { buildTaskAssert.pathIsEqualTo(":notThisPath") }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `path begins with`() {
    val buildTask: BuildTask = mock {
      on { path } doReturn TEST_PATH
    }
    val buildTaskAssert = BuildTaskAssert(buildTask)

    assertThatCode { buildTaskAssert.pathStartsWith(":task") }.doesNotThrowAnyException()
    assertThatThrownBy { buildTaskAssert.pathStartsWith(":nope") }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `path ends with`() {
    val buildTask: BuildTask = mock {
      on { path } doReturn TEST_PATH
    }
    val buildTaskAssert = BuildTaskAssert(buildTask)

    assertThatCode { buildTaskAssert.pathEndsWith("Name") }.doesNotThrowAnyException()
    assertThatThrownBy { buildTaskAssert.pathEndsWith("Nope") }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `path contains`() {
    val buildTask: BuildTask = mock {
      on { path } doReturn TEST_PATH
    }
    val buildTaskAssert = BuildTaskAssert(buildTask)

    assertThatCode { buildTaskAssert.pathContains("skNa") }.doesNotThrowAnyException()
    assertThatThrownBy { buildTaskAssert.pathContains("nope") }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `path matches`() {
    val buildTask: BuildTask = mock {
      on { path } doReturn TEST_PATH
    }
    val buildTaskAssert = BuildTaskAssert(buildTask)

    assertThatCode { buildTaskAssert.pathMatches(Pattern.compile("^:task.*$")) }.doesNotThrowAnyException()
    assertThatThrownBy { buildTaskAssert.pathMatches(Pattern.compile("^nope.*")) }.isInstanceOf(AssertionError::class.java)
  }

  @Test
  internal fun `path satisfies`() {
    val buildTask: BuildTask = mock {
      on { path } doReturn TEST_PATH
    }
    val mockConsumer: Consumer<String?> = mock()

    val buildTaskAssert = BuildTaskAssert(buildTask)
    assertThatCode { buildTaskAssert.pathSatisfies(mockConsumer) }.doesNotThrowAnyException()
    verify(mockConsumer, times(1)).accept(TEST_PATH)
  }
}
