package kr.summitsystems.springbukkit.core.command.convert

/**
 * An adapter class for pre-defined converters, such as the built-in converters in the Spring Framework.
 *
 * @param T target type of converter
 */
interface CommandArgumentConverterAdapter<T> {
    fun provideCompletes(targetType: Class<T>): List<String>
}