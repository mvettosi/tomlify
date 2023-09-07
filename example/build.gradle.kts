plugins {
  java
  id("io.github.mvettosi.tomlify")
  id("de.undercouch.download") version "5.3.0"
}

tomlify { catalogLocation.set(File(project.buildDir, "libs.versions.toml").absolutePath) }

dependencies {
  implementation("com.ibm.icu:icu4j:73.2")
  testImplementation("io.mockk:mockk:1.12.1")
}
