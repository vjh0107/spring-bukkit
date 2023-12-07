package kr.summitsystems.springbukkit.command

import java.lang.reflect.Method

sealed class CommandMappingException(
    message: String,
    val controllerClass: Class<*>,
    val mappingMethod: Method,
    val beanName: String
) : CommandException(message + " (controllerClass: ${controllerClass.name}, mappingMethod: ${mappingMethod.name}, beanName: ${beanName})") {
    class Conflict(
        conflictedRegisteredMappingQualifier: String,
        conflictedRegisteringMappingQualifier: String,
        controllerClass: Class<*>,
        mappingMethod: Method,
        beanName: String
    ) : CommandMappingException(
        "Command with qualifier ${conflictedRegisteredMappingQualifier} is conflicted with ${conflictedRegisteringMappingQualifier}.",
        controllerClass,
        mappingMethod,
        beanName
    )

    class Ambiguous(
        val controllerClassMapped: String,
        val mappingMethodMapped: String,
        controllerClass: Class<*>,
        mappingMethod: Method,
        beanName: String
    ) : CommandMappingException(
        "The class is mapped to ${controllerClassMapped}, whereas the method is mapped to ${mappingMethodMapped}.",
        controllerClass,
        mappingMethod,
        beanName
    )

    class NoMapping(
        controllerClass: Class<*>,
        mappingMethod: Method,
        beanName: String
    ) : CommandMappingException(
        "There is a mapping that does not map to any command or the plugin has more than one command.",
        controllerClass,
        mappingMethod,
        beanName
    )
}