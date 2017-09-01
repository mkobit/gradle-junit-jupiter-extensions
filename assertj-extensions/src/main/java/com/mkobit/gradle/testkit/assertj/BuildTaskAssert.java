package com.mkobit.gradle.testkit.assertj;

import org.assertj.core.api.AbstractAssert;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.TaskOutcome;

public class BuildTaskAssert extends AbstractAssert<BuildTaskAssert, BuildTask> {
  public BuildTaskAssert(final BuildTask actual) {
    super(actual, BuildTaskAssert.class);
  }

  public BuildTaskAssert pathIsEqualTo(final CharSequence fullPath) {
    isNotNull();

    if (!actual.getPath().equals(fullPath)) {
    }

    return this;
  }


  public BuildTaskAssert isFailed() {
    isNotNull();
    assertTaskOutcome(TaskOutcome.FAILED);
    return this;
  }

  public BuildTaskAssert isFromCache() {
    isNotNull();
    assertTaskOutcome(TaskOutcome.FROM_CACHE);
    return this;
  }

  public BuildTaskAssert isNoSource() {
    isNotNull();
    assertTaskOutcome(TaskOutcome.NO_SOURCE);
    return this;
  }

  public BuildTaskAssert isSkipped() {
    isNotNull();
    assertTaskOutcome(TaskOutcome.SKIPPED);
    return this;
  }

  public BuildTaskAssert isSuccess() {
    isNotNull();
    assertTaskOutcome(TaskOutcome.SUCCESS);
    return this;
  }
  public BuildTaskAssert isUpToDate() {
    isNotNull();
    assertTaskOutcome(TaskOutcome.UP_TO_DATE);
    return this;
  }

  private void assertTaskOutcome(final TaskOutcome expected) {

  }
}
