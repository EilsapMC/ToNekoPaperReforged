package suki.mrhua269.tnbr.i18n

object I18NManager {
    private var instanced: LanguageData? = null

    fun loadLanguageData(language: String) {
        val targetFileStream = I18NManager::class.java.classLoader.getResourceAsStream("lang/$language.json")

        // not found
        if (targetFileStream == null) {
            throw IllegalStateException("Could not found language $language!")
        }

        val json = targetFileStream.bufferedReader().use { it.readText() }
        instanced = LanguageData.fromJson(json)
    }

    fun getLanguageData() : LanguageData {
        return this.instanced ?: throw IllegalStateException("No language data was initialized!")
    }
}