package com.mkobit.gradle.test.jupiter

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extension
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.junit.jupiter.api.extension.TestTemplateInvocationContext
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider
import org.junit.platform.commons.support.AnnotationSupport
import org.junit.platform.commons.support.ReflectionSupport
import java.util.stream.Stream
import kotlin.reflect.KClass

@ExtendWith(GradleProjectTestTemplateInvocationContextProvider::class)
@TestTemplate
annotation class GradleProjectTestTemplate

@Repeatable
annotation class GradleProjectSource(val value: KClass<out GradleProjectProvider>)

/**
 * Implementations must provide a no-argument constructor.
 */
interface GradleProjectProvider {
  fun provideProjects(context: ExtensionContext): Stream<out GradleRunner>
}

@GradleProjectSource(GradleVersionsProvider::class)
annotation class GradleVersions(val versions: Array<String>)

class GradleVersionsProvider : GradleProjectProvider {
  override fun provideProjects(context: ExtensionContext): Stream<out GradleRunner> {
    val gradleVersions = AnnotationSupport.findAnnotation(context.requiredTestMethod, GradleVersions::class.java).orElseThrow { Exception("TODO") }
    if (gradleVersions.versions.isEmpty()) {
      // TODO: better exception
      throw Exception("${GradleVersions::class.java.simpleName} must not be empty")
    }
    return Stream.of(*gradleVersions.versions)
      .map { GradleRunner.create().withGradleVersion(it) }
  }
}

class GradleProjectTestTemplateInvocationContextProvider : TestTemplateInvocationContextProvider {
  override fun provideTestTemplateInvocationContexts(
    context: ExtensionContext
  ): Stream<TestTemplateInvocationContext> {
    val templateMethod = context.requiredTestMethod
    val sources = AnnotationSupport.findRepeatableAnnotations(templateMethod, GradleProjectSource::class.java)

    return sources.stream()
      .map { it.value }
      .map { ReflectionSupport.newInstance(it.java) }
      .flatMap { it.provideProjects(context) }
      .map { GradleProjectInvocationContext(it) }
  }

  override fun supportsTestTemplate(context: ExtensionContext): Boolean {
    return context.testMethod
      .map { AnnotationSupport.isAnnotated(it, GradleProjectTestTemplate::class.java) }
      .orElse(false)
  }
}

internal class GradleProjectInvocationContext(
  private val gradleRunner: GradleRunner
) : TestTemplateInvocationContext {
  override fun getAdditionalExtensions(): List<Extension> {
    return listOf(GradleRunnerParameterResolver(gradleRunner))
  }
}

internal class GradleRunnerParameterResolver(
  private val gradleRunner: GradleRunner
) : ParameterResolver {
  override fun supportsParameter(
    parameterContext: ParameterContext,
    extensionContext: ExtensionContext
  ): Boolean {
    return parameterContext.parameter.type == GradleRunner::class.java
  }

  override fun resolveParameter(
    parameterContext: ParameterContext,
    extensionContext: ExtensionContext
  ): Any {
    return gradleRunner
  }
}
