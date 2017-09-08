package com.mkobit.gradle.testkit.assertj;

import org.assertj.core.api.AbstractAssert;
import org.gradle.testkit.runner.BuildResult;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public class BuildResultAssert extends AbstractAssert<BuildResultAssert, BuildResult> {
  public BuildResultAssert(@Nullable final BuildResult actual) {
    super(actual, BuildResultAssert.class);
  }

  public BuildResultAssert outputContains(CharSequence text) {
    isNotNull();

    if (!actual.getOutput().contains(text)) {
      // TODO: improve message output
      failWithMessage("%nExpecting build output:%n <%s>%nto contain:%n <%s>", text, actual.getOutput());
    }

    return this;
  }

  public BuildResultAssert outputSatisfies(final Consumer<String> requirements) {
    Objects.requireNonNull(requirements, "Consumer<String> of output expressing assertions requirements muts not be null");
    requirements.accept(actual.getOutput());
    return this;
  }
}
