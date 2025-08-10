package suki.mrhua269.tnbr.utils

object FoliaDetection {
    private val FOLIA__RSERVER_CLASS_NAME = "io.papermc.paper.threadedregions.RegionizedServer"
    private val IS_FOLIA: Boolean

    init {
        var isFolia = false

        try {
            Class.forName(FOLIA__RSERVER_CLASS_NAME)
            isFolia = true
        }catch (e: ClassNotFoundException) {}

        IS_FOLIA = isFolia
    }

    fun isFolia() : Boolean {
        return IS_FOLIA
    }
}