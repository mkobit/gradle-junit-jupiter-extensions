package com.mkobit.gradle.testkit.assertj;

import org.assertj.core.api.SoftAssertions;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;

import javax.annotation.Nullable;

/**
 * Soft assertions for {@link GradleTestKitAssertions}.
 */
public final class GradleTestKitSoftAssertions extends SoftAssertions {

  public BuildTaskAssert assertThat(final @Nullable BuildTask buildTask) {
    return proxy(BuildTaskAssert.class, BuildTask.class, buildTask);
  }

  public BuildResultAssert assertThat(final @Nullable BuildResult buildResult) {
    return proxy(BuildResultAssert.class, BuildResult.class, buildResult);
  }
}
