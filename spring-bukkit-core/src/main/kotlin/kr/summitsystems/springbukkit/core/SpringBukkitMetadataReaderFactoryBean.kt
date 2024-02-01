package kr.summitsystems.springbukkit.core

import org.springframework.beans.factory.FactoryBean
import org.springframework.boot.type.classreading.ConcurrentReferenceCachingMetadataReaderFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.ResourceLoaderAware
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.core.io.ResourceLoader
import org.springframework.core.type.classreading.CachingMetadataReaderFactory

/**
 * https://github.com/spring-projects/spring-boot/pull/39321
 */
internal class SpringBukkitMetadataReaderFactoryBean : FactoryBean<ConcurrentReferenceCachingMetadataReaderFactory>, ApplicationListener<ContextRefreshedEvent>, ResourceLoaderAware {
    private var metadataReaderFactory: ConcurrentReferenceCachingMetadataReaderFactory? = null

    override fun setResourceLoader(resourceLoader: ResourceLoader) {
        metadataReaderFactory = ConcurrentReferenceCachingMetadataReaderFactory(resourceLoader)
    }

    override fun getObject(): ConcurrentReferenceCachingMetadataReaderFactory? {
        return metadataReaderFactory
    }

    override fun getObjectType(): Class<*> {
        return CachingMetadataReaderFactory::class.java
    }

    override fun isSingleton(): Boolean {
        return true
    }

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        metadataReaderFactory?.clearCache()
    }
}