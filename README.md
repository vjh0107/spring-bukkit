# Spring Bukkit Framework [![Maven Central](https://img.shields.io/maven-central/v/kr.summitsystems/spring-bukkit-core.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/kr.summitsystems/spring-bukkit-core) [![Apache License v2.0](https://img.shields.io/badge/license-Apache%20v2.0-blue.svg)](https://github.com/summit-systems/spring-bukkit/blob/main/LICENSE.txt) [![Kotlin](https://img.shields.io/badge/kotlin-1.8.22-blue.svg?logo=kotlin)](http://kotlinlang.org)

Spring Bukkit Framework is a powerful and flexible library designed to seamlessly integrate the Spring Framework with the Bukkit API, providing a robust foundation for developing Minecraft plugins with the benefits of Spring Framework.

## Key features

Leverage the full capabilities of the Spring Framework within your Bukkit plugins.
- `CommandController` WebMvc styled CommandControllers play a crucial role in modern bukkit plugin development.
- `Listener support` Bukkit Listeners will be auto registered with @BukkitListener.
- `Coroutines support` Adapting kotlinx.coroutines to be compatible with Bukkit's MainThread.
- `Configurations` The configurations for adapting Spring Framework in Bukkit plugins.
  
## Using in your projects
```kotlin
dependencies {
    implementation("kr.summitsystems:spring-bukkit-core:<version>")
}
```

#### Warning: Avoid shading the project into your plugin JAR, as it may cause a class loader issue due to Spring Framework's class loading strategy.

I recommend adding dependency notation to `plugin.yml`.

```yaml
name: ExamplePlugin
version: 1.0.0
main: org.example.exampleplugin.ExamplePlugin
libraries:
  - kr.summitsystems:spring-bukkit-core:1.0.0
```

## Documentation

* [Wiki](https://github.com/summit-systems/spring-bukkit/wiki)
* [Examples Github Repository](https://github.com/summit-systems/spring-bukkit-examples)

## License
The Spring Bukkit Framework is released under version 2.0 of the Apache License.





