package com.mkobit.gradle.testkit.assertj;

import org.assertj.core.api.AbstractAssert;
import org.gradle.testkit.runner.BuildResult;

import java.util.Objects;
import java.util.function.Consumer;

public class BuildResultAssert extends AbstractAssert<BuildResultAssert, BuildResult> {
  public BuildResultAssert(final BuildResult actual) {
    super(actual, BuildResultAssert.class);
  }

  public BuildResultAssert outputContains(CharSequence text) {
    isNotNull();

    if (!actual.getOutput().contains(text)) {
      // TODO: improve message output
      failWithMessage("Expected build output to contain <%s> but did not", text);
    }

    return this;
  }

  public BuildResultAssert outputSatisfies(Consumer<String> requirements) {
    Objects.requireNonNull(requirements, "Consmer<String> of output expressing assertions requirements muts not be null");
    requirements.accept(actual.getOutput());
    return this;
  }
}
