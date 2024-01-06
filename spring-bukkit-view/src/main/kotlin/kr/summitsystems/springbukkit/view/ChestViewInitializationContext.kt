package kr.summitsystems.springbukkit.view

open class ChestViewInitializationContext(
    private val raw: Int,
    private val title: String
) : GenericViewInitializationContext() {
    init {
        require(raw in 1..6)
    }

    fun getRaw(): Int {
        return raw
    }

    fun getTitle(): String {
        return title
    }
}