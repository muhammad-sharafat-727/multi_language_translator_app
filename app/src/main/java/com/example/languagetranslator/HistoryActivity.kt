package com.example.languagetranslator

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.languagetranslator.adapters.HistoryAdapter
import com.example.languagetranslator.data.PreferenceManage
import com.example.languagetranslator.databinding.ActivityHistoryBinding
import com.example.languagetranslator.home.HomeActivity

class HistoryActivity : AppCompatActivity() {
    private lateinit var adapter: HistoryAdapter
    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)

        val pref = PreferenceManage(this)
        val conversations = pref.getAllConversations()

        if(conversations.isEmpty())
        {
            binding.rvAllConversations.visibility = View.GONE
            binding.tvNotDataFound.visibility =View.VISIBLE
        }

        adapter = HistoryAdapter(this,conversations, { position ->
            conversations[position].isFavourate = !conversations[position].isFavourate
            pref.saveAllConversations(conversations)
            adapter.notifyItemChanged(position)

        },
        { position ->

            val intent = Intent(this,DetailedActivity::class.java)
            intent.putExtra("srcText",conversations[position].srcText)
            intent.putExtra("destText",conversations[position].destText)
            intent.putExtra("srcLanguage",conversations[position].srcLanguage)
            intent.putExtra("destLanguage",conversations[position].destLanguage)
            intent.putExtra("isFavourite",conversations[position].isFavourate)
            startActivity(intent)

        })

        binding.rvAllConversations.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAllConversations.adapter = adapter
        wireBottomNav(
            binding.topNav.root,
            NavTarget.HISTORY,
            homeActivity = HomeActivity::class.java,
            biLanguageTranslationActivity = BiLanguageTranslationActivity::class.java,
            historyActivity = HistoryActivity::class.java,
            starActivity = StarActivity::class.java
        )

        binding.btnClearAll.setOnClickListener {
             AlertDialog.Builder(this).setTitle("Are you want to delete All Conversation").setPositiveButton("Yes"){_,_ ->
                pref.clearAllConversation()
                 binding.rvAllConversations.visibility = View.GONE
                 binding.tvNotDataFound.visibility =View.VISIBLE
                 adapter.updateData()

            }.setNegativeButton("No"){_,_ ->

            }.show()
        }

    }


}