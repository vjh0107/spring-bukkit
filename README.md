<img src="https://github.com/summit-systems/spring-bukkit/raw/main/project-icon.png?size=96" alt="SpringBukkit" title="SpringBukkit" align="right"/>

# Spring Bukkit Framework 
[![Maven Central](https://img.shields.io/maven-central/v/kr.summitsystems/spring-bukkit-core.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/kr.summitsystems/spring-bukkit-core) [![Apache License v2.0](https://img.shields.io/badge/license-Apache%20v2.0-blue.svg)](https://github.com/summit-systems/spring-bukkit/blob/main/LICENSE.txt) [![Kotlin](https://img.shields.io/badge/kotlin-1.8.22-blue.svg?logo=kotlin)](http://kotlinlang.org) [![Discord](https://img.shields.io/discord/1180490815248801833?logo=discord&label=Discord)](https://discord.gg/nvzx939V2T)

Spring Bukkit Framework is a powerful and flexible library designed to seamlessly integrate the Spring Framework with the Bukkit API, providing a robust foundation for developing Minecraft plugins with the benefits of Spring Framework.

## Key features

Leverage the full capabilities of the Spring Framework within your Bukkit plugins.
- `CommandController` WebMvc styled CommandControllers play a crucial role in modern bukkit plugin development.
- `View` Navigatable view leads to making the user experience richer.
- `Listener support` Bukkit Listeners will be auto registered.
- `Coroutines support` Adapting kotlinx.coroutines to be compatible with Bukkit's MainThread.
- `JPA Support` Provide Bukkit dedicated JPA converters.

## Using in your projects
```kotlin
dependencies {
    implementation("kr.summitsystems:spring-bukkit-starter:<version>")
}
```

#### Warning: Avoid shading the project into your plugin JAR, as it may cause a class loader issue due to Spring Framework's class loading strategy.

I recommend adding dependency notation to `plugin.yml`.

```yaml
name: ExamplePlugin
version: 1.0.0
main: org.example.exampleplugin.ExamplePlugin
libraries:
  - kr.summitsystems:spring-bukkit-starter:<version>
```

## Documentation

* [Wiki](https://github.com/summit-systems/spring-bukkit/wiki)
* [Examples Github Repository](https://github.com/summit-systems/spring-bukkit-examples)

## Module dependency graph

![Dependency Graph](https://github.com/summit-systems/spring-bukkit/raw/main/project-dependency-graph.png)

## License
The Spring Bukkit Framework is released under version 2.0 of the Apache License.
