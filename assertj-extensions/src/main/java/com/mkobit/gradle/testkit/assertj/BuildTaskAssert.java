package com.mkobit.gradle.testkit.assertj;

import org.assertj.core.api.AbstractAssert;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.TaskOutcome;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class BuildTaskAssert extends AbstractAssert<BuildTaskAssert, BuildTask> {
  public BuildTaskAssert(@Nullable final BuildTask actual) {
    super(actual, BuildTaskAssert.class);
  }

  public BuildTaskAssert pathIsEqualTo(final String path) {
    isNotNull();
    if (!path.equals(actual.getPath())) {
      failWithMessage("%nExpecting task path to be be equal to:%n <%s>%nbut was:%n <%s>", path, actual.getPath());
    }
    return this;
  }

  public BuildTaskAssert pathStartsWith(final CharSequence pathPrefix) {
    isNotNull();

    final String pathValue = pathPrefix.toString();
    if (!actual.getPath().startsWith(pathValue)) {
      failWithMessage("%nExpecting task path to start with:%n <%s>%nbut was:%n <%s>", pathValue, actual.getPath());
    }
    return this;
  }

  public BuildTaskAssert pathEndsWith(final CharSequence pathPrefix) {
    isNotNull();

    final String pathValue = pathPrefix.toString();
    if (!actual.getPath().endsWith(pathValue)) {
      failWithMessage("%nExpecting task path to end with:%n <%s>%nbut was:%n <%s>", pathValue, actual.getPath());
    }
    return this;
  }

  public BuildTaskAssert pathContains(final CharSequence pathPrefix) {
    isNotNull();

    final String pathValue = pathPrefix.toString();
    if (!actual.getPath().contains(pathValue)) {
      failWithMessage("%nExpecting task path to contain:%n <%s>%nbut was:%n <%s>", pathValue, actual.getPath());
    }
    return this;
  }

  public BuildTaskAssert pathMatches(final Pattern pattern) {
    isNotNull();

    if (!pattern.matcher(actual.getPath()).matches()) {
      failWithMessage("%nExpecting task path to match:%n <%s>%nbut was:%n <%s>", pattern, actual.getPath());
    }
    return this;
  }

  public BuildTaskAssert pathSatisfies(final Consumer<String> requirements) {
    Objects.requireNonNull(requirements, "The consumer providing requirements on the task path must not be null");
    requirements.accept(actual.getPath());
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

  public BuildTaskAssert hasTaskOutcome(final TaskOutcome taskOutcome) {
    isNotNull();
    assertTaskOutcome(taskOutcome);
    return this;
  }

  private void assertTaskOutcome(final TaskOutcome expected) {
    if (actual.getOutcome() != expected) {
      //failWithMessage("%nExpecting build output:%n <%s>%nto contain:%n <%s>", text, actual.getOutput());
      failWithMessage(
        "%nExpecting task at path %s to have outcome:%n <%s>%nbut was:%n <%s>",
        actual.getPath(),
        expected,
        actual.getOutcome()
      );
    }
  }
}
