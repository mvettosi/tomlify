package io.github.mvettosi.tomlify.di

import io.github.mvettosi.tomlify.data.gradle.GradleGroovySource
import io.github.mvettosi.tomlify.data.gradle.GradleKtsSource
import io.github.mvettosi.tomlify.data.toml.TomlCatalogSource
import io.github.mvettosi.tomlify.domain.source.CatalogSource
import io.github.mvettosi.tomlify.domain.source.GradleSource
import io.github.mvettosi.tomlify.domain.usecase.Tomlify
import io.github.mvettosi.tomlify.domain.usecase.extract.ExtractDependencies
import io.github.mvettosi.tomlify.domain.usecase.extract.ExtractGradleGroovy
import io.github.mvettosi.tomlify.domain.usecase.extract.ExtractGradleKts
import io.github.mvettosi.tomlify.domain.usecase.replace.ReplaceDependencies
import io.github.mvettosi.tomlify.domain.usecase.replace.ReplaceGradkeKts
import io.github.mvettosi.tomlify.domain.usecase.replace.ReplaceGradleGroovy
import io.github.mvettosi.tomlify.domain.usecase.update.SetupCatalog
import io.github.mvettosi.tomlify.domain.usecase.update.UpdateCatalog
import java.io.File
import org.gradle.api.Project
import org.gradle.api.provider.Property

object AppInjection {
  private lateinit var project: Project
  private lateinit var tomlLocation: Property<String>

  fun init(project: Project, tomlLocation: Property<String>) {
    this.project = project
    this.tomlLocation = tomlLocation
  }

  // Data Sources
  private val tomCatalogSource: CatalogSource by lazy {
    TomlCatalogSource(tomlLocation = File(tomlLocation.get()))
  }
  private val gradleKtsSource: GradleSource by lazy { GradleKtsSource(project = project) }
  private val gradleGroovySource: GradleSource by lazy { GradleGroovySource(project = project) }

  // Use Cases
  private val extractGradleKts by lazy { ExtractGradleKts(gradleSource = gradleKtsSource) }
  private val extractGradleGroovy by lazy { ExtractGradleGroovy(gradleSource = gradleGroovySource) }
  private val extractDependencies by lazy {
    ExtractDependencies(
        extractGradleKts = extractGradleKts, extractGradleGroovy = extractGradleGroovy)
  }

  private val setupCatalog by lazy { SetupCatalog(catalogSource = tomCatalogSource) }
  private val updateCatalog by lazy {
    UpdateCatalog(setupCatalog = setupCatalog, catalogSource = tomCatalogSource)
  }

  private val replaceGradkeKts by lazy { ReplaceGradkeKts(gradleSource = gradleKtsSource) }
  private val replaceGradleGroovy by lazy { ReplaceGradleGroovy(gradleSource = gradleGroovySource) }
  private val replaceDependencies by lazy {
    ReplaceDependencies(
        replaceGradkeKts = replaceGradkeKts, replaceGradleGroovy = replaceGradleGroovy)
  }

  val tomlify by lazy {
    Tomlify(
        extractDependencies = extractDependencies,
        updateCatalog = updateCatalog,
        replaceDependencies = replaceDependencies)
  }
}
