package com.example.languagetranslator.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.languagetranslator.R
import com.example.languagetranslator.data.Conversation
import com.example.languagetranslator.data.Helper
import com.example.languagetranslator.databinding.ItemTranslatedBinding

class HistoryAdapter(
    private val context: Context, private var allConversation: MutableList<Conversation>,
    private val onButtonClick: (position: Int) -> Unit,
    private val onButtonCardClick:(currentPosition: Int) -> Unit,
) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    @SuppressLint("NotifyDataSetChanged")
    fun updateData() {
        allConversation.clear()
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(val binding: ItemTranslatedBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            ItemTranslatedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)

    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val conversation = allConversation[position]

        holder.binding.tvSrcText.text = conversation.srcText
        holder.binding.tvDestText.text = conversation.destText
        holder.binding.ivSrcImg.setImageResource(
            Helper.getLanguageFlagResource(
                context,
                conversation.srcLanguage
            )
        )
        holder.binding.ivDestImg.setImageResource(
            Helper.getLanguageFlagResource(
                context,
                conversation.destLanguage
            )
        )

        if (conversation.isFavourate) {
            holder.binding.imgIsFavourate.setImageResource(R.drawable.ic_star_filled) // Change to your filled/colored star resource name
        } else {
            holder.binding.imgIsFavourate.setImageResource(R.drawable.ic_star)
        }


        holder.binding.imgIsFavourate.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                onButtonClick(currentPosition)
            }
        }

        holder.binding.cvConversation.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                onButtonCardClick(currentPosition)
            }
        }


    }




    override fun getItemCount() = allConversation.size
}