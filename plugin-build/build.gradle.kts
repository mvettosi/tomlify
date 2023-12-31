plugins {
  alias(libs.plugins.kotlin) apply false
  alias(libs.plugins.pluginPublish) apply false
}

allprojects {
  group = property("GROUP").toString()
  version = property("VERSION").toString()
}

tasks.register("clean", Delete::class.java) { delete(rootProject.buildDir) }

tasks.wrapper { distributionType = Wrapper.DistributionType.ALL }
