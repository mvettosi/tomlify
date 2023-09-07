import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
  alias(libs.plugins.kotlin) apply false
  alias(libs.plugins.spotless)
}

tasks.findByName("clean")?.doLast { delete(rootProject.buildDir) }

tasks.register("preMerge") {
  description = "Runs all the tests/verification tasks on both top level and included build."

  dependsOn(":example:check")
  dependsOn(gradle.includedBuild("plugin-build").task(":plugin:check"))
  dependsOn(gradle.includedBuild("plugin-build").task(":plugin:validatePlugins"))
}

tasks.wrapper { distributionType = Wrapper.DistributionType.ALL }

configure<SpotlessExtension> {
  kotlin {
    target("**/src/**/*.kt")
    ktfmt()
  }
  kotlinGradle {
    target("**/*.gradle.kts")
    ktfmt()
  }
}
