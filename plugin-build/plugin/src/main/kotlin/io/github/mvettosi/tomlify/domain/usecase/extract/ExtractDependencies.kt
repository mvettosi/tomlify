package io.github.mvettosi.tomlify.domain.usecase.extract

class ExtractDependencies(
    val extractGradleKts: ExtractGradleKts,
    val extractGradleGroovy: ExtractGradleGroovy
) {
  operator fun invoke() = extractGradleKts() + extractGradleGroovy()
}
