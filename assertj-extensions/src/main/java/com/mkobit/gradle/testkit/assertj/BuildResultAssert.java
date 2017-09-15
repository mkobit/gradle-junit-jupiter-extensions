package com.mkobit.gradle.testkit.assertj;

import org.assertj.core.api.AbstractAssert;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.TaskOutcome;

import javax.annotation.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BuildResultAssert extends AbstractAssert<BuildResultAssert, BuildResult> {
  public BuildResultAssert(final @Nullable BuildResult actual) {
    super(actual, BuildResultAssert.class);
  }

  //  String getOutput();
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

  public BuildResultAssert hasTaskAtPathSatisfying(final CharSequence path, final Consumer<? super BuildTask> requirements) {
    isNotNull();
    Objects.requireNonNull(requirements, "Consumer<BuildTask> of task expressing assertions requirements must not be null");
    final String taskPath = path.toString();
    final BuildTask buildTask = actual.task(taskPath);
    requirements.accept(buildTask);
    return this;
  }

  public BuildResultAssert tasksWithOutcomeSatisfy(
    final TaskOutcome outcome,
    final Consumer<List<BuildTask>> requirements
  ) {
    isNotNull();
    Objects.requireNonNull(outcome, "outcome must not be null");
    Objects.requireNonNull(requirements, "requirements must not be null");
    requirements.accept(actual.tasks(outcome));
    return this;
  }

  public BuildResultAssert taskPathsWithOutcomeSatisfy(
    final TaskOutcome outcome,
    final Consumer<List<String>> requirements
  ) {
    isNotNull();
    Objects.requireNonNull(outcome, "outcome must not be null");
    Objects.requireNonNull(requirements, "requirements must not be null");
    requirements.accept(actual.taskPaths(outcome));
    return this;
  }

  public BuildResultAssert tasksSatisfy(final Consumer<List<BuildTask>> requirements) {
    isNotNull();
    Objects.requireNonNull(requirements, "requirements must not be null");
    requirements.accept(actual.getTasks());
    return this;
  }

  public BuildResultAssert hasTaskFailedAtPath(final CharSequence path) {
    return hasTaskAtPathWithOutcome(path, TaskOutcome.FAILED);
  }

  public BuildResultAssert hasTaskFromCacheAtPath(final CharSequence path) {
    return hasTaskAtPathWithOutcome(path, TaskOutcome.FROM_CACHE);
  }

  public BuildResultAssert hasTaskNoSourceAtPath(final CharSequence path) {
    return hasTaskAtPathWithOutcome(path, TaskOutcome.NO_SOURCE);
  }

  public BuildResultAssert hasTaskSkippedAtPath(final CharSequence path) {
    return hasTaskAtPathWithOutcome(path, TaskOutcome.SKIPPED);
  }

  public BuildResultAssert hasTaskSuccessAtPath(final CharSequence path) {
    return hasTaskAtPathWithOutcome(path, TaskOutcome.SUCCESS);
  }

  public BuildResultAssert hasTaskUpToDateAtPath(final CharSequence path) {
    return hasTaskAtPathWithOutcome(path, TaskOutcome.UP_TO_DATE);
  }

  public BuildResultAssert hasTaskAtPathWithOutcome(final CharSequence path, final TaskOutcome outcome) {
    isNotNull();
    Objects.requireNonNull(outcome, "outcome must not be null");
    final String taskPath = path.toString();
    final BuildTask buildTask = actual.task(taskPath);
    if (buildTask == null) {
      failWithMessage(
        "Build task at path %s was not executed so could not be checked for outcome %s.%nTasks executed by build were:%n %s", taskPath, outcome, formatTasksForFailureMessage(actual.getTasks()));
    }
    if (buildTask.getOutcome() != outcome) {
      failWithMessage("Build task at path %s expected to have outcome:%n <%s>%nbut was:%n <%s>", taskPath, outcome, buildTask.getOutcome());
    }
    return this;
  }

  private String formatTasksForFailureMessage(final List<BuildTask> tasks) {
    return tasks.stream()
                .sorted(Comparator.comparing(BuildTask::getPath))
                .map(buildTask -> "(path=" + buildTask.getPath() + ", " + "outcome=" + buildTask.getOutcome())
                .collect(Collectors.joining(", ", "[", "]"));
  }
}
