package com.mkobit.gradle.testkit.assertj;

import org.assertj.core.api.AbstractAssert;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public class BuildResultAssert extends AbstractAssert<BuildResultAssert, BuildResult> {
  public BuildResultAssert(final @Nullable BuildResult actual) {
    super(actual, BuildResultAssert.class);
  }

  //  String getOutput();
  // TODO: look at assertJ feature for breaking method change for contains() for reasoning why it is being change
  public BuildResultAssert outputContains(final CharSequence sequence) {
    isNotNull();

    if (!actual.getOutput().contains(sequence)) {
      failWithMessage("%nExpecting build result output:%n <%s>%nto contain sequence:%n <%s>", actual.getOutput(), sequence);
    }

    return this;
  }

  public BuildResultAssert outputDoesNotContain(final CharSequence sequence) {
    isNotNull();

    if (actual.getOutput().contains(sequence)) {
      failWithMessage("%nExpecting build result output:%n <%s>%nto not contain sequence:%n <%s>", actual.getOutput(), sequence);
    }

    return this;
  }

  public BuildResultAssert outputSatisfies(final Consumer<String> requirements) {
    Objects.requireNonNull(requirements, "Consumer<String> of output expressing assertions requirements must not be null");
    requirements.accept(actual.getOutput());
    return this;
  }
  //  List<String> taskPaths(TaskOutcome var1);
  //  List<BuildTask> tasks(TaskOutcome var1);
  //  List<BuildTask> getTasks();

  //  @Nullable
  //  BuildTask task(String var1);

  public BuildResultAssert hasTaskAtPath(final CharSequence path) {
    final String taskPath = path.toString();
    final BuildTask buildTask = actual.task(taskPath);
    if (buildTask == null) {
      failWithMessage("%nExpecting build to have task at path:%n <%s>%nbut did not", taskPath);
    }
    return this;
  }

  public BuildResultAssert doesNotHaveTaskAtPath(final CharSequence path) {
    final String taskPath = path.toString();
    final BuildTask buildTask = actual.task(taskPath);
    if (buildTask != null) {
      failWithMessage("%nExpecting build to not have task at path:%n <%s>%nbut did", taskPath);
    }
    return this;
  }

  public BuildResultAssert hasTaskAtPathSatisfying(final CharSequence path, final Consumer<BuildTask> requirements) {
    Objects.requireNonNull(requirements, "Consumer<BuildTask> of task expressing assertions requirements must not be null");
    final String taskPath = path.toString();
    final BuildTask buildTask = actual.task(taskPath);
    requirements.accept(buildTask);
    return this;
  }
}
