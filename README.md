# Tomlify
A Gradle plugin that migrates all legacy dependency declarations in all *.kts files to a TOML version catalog.
Currently only files in Kotlin DSL (`*.kts`) are supported.

# Installation
Add the following dependency to your build.gradle.kts file:

```kotlin
plugins {
  id("io.github.mvettosi.tomlify") version "1.0.0"
}
```

# Usage
The plugin should be applied in the root build.gradle.kts file.
The `tomlify` task will look for all supported configuration files (currently all `*.kts` files in the project), extract all dependency and plugin declarations it can understand, organise them in a catalog then replace usages with the version catalog sintax.
The first time it is ran it will also create the catalog if it's missing, and any successive run will pick up only the new dependency declarations that have been added since last usage.

The plugin can be configured through the `tomlify` block, which currently only includes the property `catalogLocation` which indicates the path relative to the root of the project of the catalog to use.

For example, to use the tomlify task with the catalog located at `./catalog.toml`, you would add the following to your root `build.gradle.kts` file:

```kotlin
tomlify {
  catalogLocation = "./catalog.toml"
}
```

# Ignoring Lines
The `tomlify` task automatically ignores commented lines and specific lines can be avoided by appending `//no-tomlify` to them.

For example, the following line would be ignored:

```kotlin
  // implementation("some.group:some.name:1.2.3")
  implementation("some.group:some.name:1.2.3") //no-tomlify
```

# Support for Groovy
The project currently doesn't support Groovy gradle files but implementation can be easily added if I were to receive request for it. Of course, you are also free (and encouraged!) to open a PR yourself if you'd like to. All it really needs is implementation of the following files:
- `GradleGroovySource`
- `ExtractGradleGroovy`
- `ReplaceGradleGroovy`

# Bugs or unrecognised declarations
Automatically covering all possible declaration proved to be an impossible task without risking to over-do it and mess with the wrong lines, so instead I opted for an enumerated approach where only the known types of declarations are processed.
If some of the legacy declarations in your project are being ignored by the task, please open an Issue pasting the line that was ignored and I'll make sure to add support for it as soon as I realistically can.
Alternatively, you are more than welcome to submit support for it yourself by adding the corresponding line in `GradleConstants.kts` file and, if you please, an automated test in the `ExtractGradleKtsTest.kts` file, too.
