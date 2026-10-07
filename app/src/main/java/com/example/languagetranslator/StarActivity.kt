package com.example.languagetranslator

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.languagetranslator.adapters.HistoryAdapter
import com.example.languagetranslator.data.PreferenceManage
import com.example.languagetranslator.databinding.ActivityStarBinding
import com.example.languagetranslator.home.HomeActivity

class StarActivity : AppCompatActivity() {
    private lateinit var adapter: HistoryAdapter
    private lateinit var binding: ActivityStarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)

        upDateUi()
        val pref = PreferenceManage(this)
        val masterList = pref.getAllConversations()
        val displayList = pref.getAllFavourateConversations()

        adapter = HistoryAdapter(
            this, displayList, { position ->

                val clickedItem = displayList[position]
                val masterItem = masterList.find {
                    it.srcText == clickedItem.srcText && it.destText == clickedItem.destText
                }

                masterItem?.let {
                    it.isFavourate = !it.isFavourate
                    pref.saveAllConversations(masterList)
                    displayList.removeAt(position)
                    upDateUi()
                    adapter.notifyItemRemoved(position)
                }
                adapter.notifyItemChanged(position)

            },
            { position ->

                val intent = Intent(this, DetailedActivity::class.java)
                intent.putExtra("srcText", displayList[position].srcText)
                intent.putExtra("destText", displayList[position].destText)
                intent.putExtra("srcLanguage", displayList[position].srcLanguage)
                intent.putExtra("destLanguage", displayList[position].destLanguage)
                intent.putExtra("isFavourite", displayList[position].isFavourate)
                startActivity(intent)

            })


        binding.rvStaredConversations.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvStaredConversations.adapter = adapter
        wireBottomNav(
            binding.topNav.root,
            NavTarget.STAR,
            homeActivity = HomeActivity::class.java,
            biLanguageTranslationActivity = BiLanguageTranslationActivity::class.java,
            historyActivity = HistoryActivity::class.java,
            starActivity = StarActivity::class.java
        )

        binding.btnClearAll.setOnClickListener {
            if (pref.getAllFavourateConversations().isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Are you want to delete all favourite conversations")
                    .setPositiveButton("Yes") { _, _ ->
                        adapter.updateData()
                        pref.clearAllFavourateConversations()
                        upDateUi()
                    }
                    .setNegativeButton("No") { _, _ -> }.show()
            } else {
                Toast.makeText(this, "Add Record First", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun upDateUi() {
        val pref = PreferenceManage(this)
        if (pref.getAllFavourateConversations().isNotEmpty()) {
            binding.rvStaredConversations.visibility = View.VISIBLE
            binding.tvNotDataFound.visibility = View.GONE
        } else {
            binding.rvStaredConversations.visibility = View.GONE
            binding.tvNotDataFound.visibility = View.VISIBLE
        }
    }
}