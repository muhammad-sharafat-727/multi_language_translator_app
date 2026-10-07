package com.example.languagetranslator.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Conversation(
    val srcText: String,
    val destText: String,
    val srcLanguage: String,
    val destLanguage: String,
    var isFavourate: Boolean = false,
    val sequenceNumber: Int = -1,
    val side: Char = 'l',
    val timestamp: Long = System.currentTimeMillis()
)

class PreferenceManage(context: Context) {
    private val gson = Gson()
    private val pref: SharedPreferences =
        context.getSharedPreferences("language_translation", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CONVERSATION_TEXT = "conversation_text"
        private const val KEY_MESSAGE_COUNT = "message_count"
    }

    fun saveAllConversations(conversations: List<Conversation>) {
        pref.edit().putString(KEY_CONVERSATION_TEXT, gson.toJson(conversations)).apply()
    }

    fun saveConverstion(conversation: Conversation) {
        val prevAllConversation = getAllConversations()
        prevAllConversation.add(conversation)
        saveAllConversations(prevAllConversation)
    }

    fun upDateSequenceNo(value: Int) {
        pref.edit().putInt(KEY_MESSAGE_COUNT, value).apply()
    }

    fun getSequenceNo(): Int {
        val seqNo = pref.getInt(KEY_MESSAGE_COUNT, 0)
        upDateSequenceNo(seqNo + 1)
        return seqNo
    }


    fun getAllConversations(): MutableList<Conversation> {
        val strObj = pref.getString(KEY_CONVERSATION_TEXT, null)
        val type = object : TypeToken<MutableList<Conversation>>() {}.type
        Log.d("Conversation.kt", "Conversation Return Successfully")
        return gson.fromJson<MutableList<Conversation>>(strObj, type)
            ?: mutableListOf<Conversation>()
    }

    fun getAllFavourateConversations(): MutableList<Conversation> {
        return getAllConversations().filter { conversation -> conversation.isFavourate } as MutableList<Conversation>
    }

    fun getOtherThanFavourite(): MutableList<Conversation> {
        return getAllConversations().filter { conversation -> !conversation.isFavourate } as MutableList<Conversation>
    }

    fun clearAllConversation() {
        pref.edit().clear().apply()
    }

    fun clearAllFavourateConversations() {
        val otherConversations = getOtherThanFavourite()
        clearAllConversation()
        saveAllConversations(otherConversations)

    }

    fun getAllSequenceConversations(
        srcLanguage: String, targetLanguage: String
    ): MutableList<Conversation> {
        val allConversation = getAllConversations()
        return allConversation.filter { conversation -> conversation.sequenceNumber != -1 && conversation.srcLanguage == srcLanguage && conversation.destLanguage == targetLanguage || conversation.destLanguage == srcLanguage && conversation.srcLanguage == targetLanguage }
            .toMutableList()

    }

    fun markAsFavourite() {
        val allConversations = getAllConversations().sortedByDescending { it.timestamp }
        allConversations[0].isFavourate = !allConversations[0].isFavourate;
        saveAllConversations(allConversations)

    }

    fun clearAllConversationBySrcAndTarget(srcLanguage: String, targetLanguage: String) {
        val allConversation = getAllConversations()

        val filterConversation = allConversation.filter { conversation ->
            val isExactMatch = conversation.srcLanguage == srcLanguage && conversation.destLanguage == targetLanguage
            val isReverseMatch = conversation.srcLanguage == targetLanguage && conversation.destLanguage == srcLanguage

            !isExactMatch && !isReverseMatch
        }.toMutableList()

        saveAllConversations(filterConversation)
    }

    fun anyConversationFound(srcLanguage: String, destLanguage: String): Boolean {
        return getAllSequenceConversations(srcLanguage, destLanguage).isNotEmpty()
    }


}