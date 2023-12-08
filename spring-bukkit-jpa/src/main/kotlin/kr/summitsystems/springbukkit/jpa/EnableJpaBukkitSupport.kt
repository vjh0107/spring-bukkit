package kr.summitsystems.springbukkit.jpa

import org.springframework.context.annotation.Import

@Import(SpringBukkitJpaConfiguration::class)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EnableJpaBukkitSupport