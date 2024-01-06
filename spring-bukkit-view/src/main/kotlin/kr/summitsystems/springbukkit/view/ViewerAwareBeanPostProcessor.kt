package kr.summitsystems.springbukkit.view

import org.bukkit.entity.Player
import org.springframework.beans.factory.config.BeanPostProcessor

class ViewerAwareBeanPostProcessor(private val viewer: Player) : BeanPostProcessor {
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        if (bean !is ViewerAware) {
            return bean
        }
        bean.setViewer(viewer)
        return bean
    }
}