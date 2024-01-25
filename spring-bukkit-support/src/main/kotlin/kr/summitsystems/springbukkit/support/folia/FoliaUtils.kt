package kr.summitsystems.springbukkit.support.folia

object FoliaUtils {
    fun isUsingFolia(): Boolean {
        return classExists("io.papermc.paper.threadedregions.RegionizedServer")
    }

    private fun classExists(className: String): Boolean {
        return try {
            Class.forName(className)
            true
        } catch (ex: ClassNotFoundException) {
            false
        }
    }
}