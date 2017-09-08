package com.mkobit.gradle.testkit.assertj;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;

import javax.annotation.Nullable;

public final class GradleTestKitAssertions {
  private GradleTestKitAssertions() {
  }

  static BuildTaskAssert assertThat(final @Nullable BuildTask buildTask) {
    return new BuildTaskAssert(buildTask);
  }

  static BuildResultAssert assertThat(final @Nullable BuildResult buildResult) {
    return new BuildResultAssert(buildResult);
  }
}
