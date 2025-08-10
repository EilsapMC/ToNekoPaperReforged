package suki.mrhua269.tnbr.i18n

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

data class LanguageData(
    @SerializedName("message_neko_player_prefix")
    val messageNekoPlayerPrefix: String,
    @SerializedName("message_neko_master_word")
    val messageMasterWord: String,
    @SerializedName("message_neko_player_chat_format")
    val messageNekoPlayerChatFormat: String,
    @SerializedName("message_neko_player_chat_meow")
    val messageNekoPlayerMeow: String,
    @SerializedName("message_hover_event_of_neko_prefix")
    val messageHoverEventOfNekoPrefix: String,
    @SerializedName("message_hover_event_of_neko_no_master")
    val messageHoverEventOfNekoNoMaster: String,
) {
    companion object {
        val GSON: Gson = GsonBuilder().setPrettyPrinting().create()

        fun fromJson(json: String): LanguageData {
            return GSON.fromJson(json, LanguageData::class.java)
        }
    }
}
