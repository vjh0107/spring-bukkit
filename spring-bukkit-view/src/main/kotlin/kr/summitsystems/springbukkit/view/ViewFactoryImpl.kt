package kr.summitsystems.springbukkit.view

import org.bukkit.entity.Player
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Role
import org.springframework.context.support.GenericApplicationContext
import org.springframework.stereotype.Component

@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Component
class ViewFactoryImpl(private val applicationContext: ApplicationContext) : ViewFactory {
    override fun <C : ViewInitializationContext> create(viewer: Player, view: Class<out View<C>>, context: C): View<C> {
        val childContext = GenericApplicationContext(applicationContext)
        val beanFactory = childContext.beanFactory
        beanFactory.addBeanPostProcessor(ViewerAwareBeanPostProcessor(viewer = viewer))
        childContext.refresh()
        return beanFactory.createBean(view).also { instantiatedView ->
            instantiatedView.initializeInventory(context)
        }
    }
}