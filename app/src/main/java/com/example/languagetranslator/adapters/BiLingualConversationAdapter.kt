package com.example.languagetranslator.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.languagetranslator.data.Conversation
import com.example.languagetranslator.data.Helper
import com.example.languagetranslator.databinding.ItemConversationBinding

class BiLingualConversationAdapter(val context : Context , val masterList : MutableList<Conversation>) : RecyclerView.Adapter<BiLingualConversationAdapter.BiLingualConversationViewHolder>() {

    fun updateList(newList : MutableList<Conversation>)
    {
        masterList.clear()
        masterList.addAll(newList)
        notifyDataSetChanged()
    }
    inner  class BiLingualConversationViewHolder(val binding : ItemConversationBinding ) : RecyclerView.ViewHolder(binding.root)

    override  fun onCreateViewHolder(parent : ViewGroup, viewTypr : Int) : BiLingualConversationViewHolder
    {
        val binding = ItemConversationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BiLingualConversationViewHolder(binding)
    }


    override fun onBindViewHolder(holder: BiLingualConversationViewHolder, position: Int) {
        val conversation = masterList[position]

        val flag = holder.binding.languageFlag.layoutParams as GridLayout.LayoutParams
        val talk = holder.binding.cvConversation.layoutParams as GridLayout.LayoutParams

        if (conversation.side == 'r') {
            flag.columnSpec = GridLayout.spec(1,)
            flag.setMarginStart(12)
            talk.columnSpec = GridLayout.spec(0,8f)
        } else {
            flag.columnSpec = GridLayout.spec(0,)
            talk.columnSpec = GridLayout.spec(1,8f)
            talk.setMarginStart(12)
        }


        holder.binding.languageFlag.layoutParams = flag
        holder.binding.cvConversation.layoutParams = talk

        holder.binding.talkFlag.setImageResource(Helper.getLanguageFlagResource(context, conversation.srcLanguage))
        holder.binding.tvSrcText.text = conversation.srcText
        holder.binding.tvDestText.text = conversation.destText
        holder.binding.srcLanguge.text = Helper.getMLKitLanguageCode(conversation.srcLanguage)
    }

    override fun getItemCount() = masterList.size
}