package suki.mrhua269.tnbr.i18n

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

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
    @SerializedName("message_command_wrong_use")
    val messageWrongUse: String,
    @SerializedName("message_player_not_found")
    val messagePlayerNotFound: String,
    @SerializedName("message_item_not_found_give_command")
    val messageItemNotFoundGiveCommand: String,
    @SerializedName("message_no_permission")
    val messageNoPermission: String,
    @SerializedName("message_neko_set_to_a_neko")
    val messageNekoSetToANeko: String,
    @SerializedName("message_neko_set_to_not_a_neko")
    val messageNekoSetToNotANeko: String,
    @SerializedName("message_nothing_has_changed")
    val messageNothingHasChanged: String,

) {
    companion object {
        val GSON: Gson = GsonBuilder().setPrettyPrinting().create()

        fun fromJson(json: String): LanguageData {
            return GSON.fromJson(json, LanguageData::class.java)
        }

        fun miniMessaged(input: String) : Component {
            return MiniMessage.miniMessage().deserialize(input)
        }

        fun miniMessaged(input: String, tagResolver: TagResolver) : Component {
            return MiniMessage.miniMessage().deserialize(input, tagResolver)
        }
    }
}
