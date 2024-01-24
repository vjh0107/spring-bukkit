package kr.summitsystems.springbukkit.support.protocollib

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProtocolLibConfiguration {
    @ConditionalOnClass(name = ["com.comphenix.protocol.ProtocolManager"])
    @Bean
    fun protocolManager(): ProtocolManager {
        return ProtocolLibrary.getProtocolManager()
    }
}