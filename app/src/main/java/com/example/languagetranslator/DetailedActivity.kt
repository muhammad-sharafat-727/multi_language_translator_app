package com.example.languagetranslator

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.languagetranslator.data.Helper
import com.example.languagetranslator.databinding.ActivityDetailedBinding

class DetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)

        val srcText = intent.getStringExtra("srcText").toString()
        val destText = intent.getStringExtra("destText").toString()
        val srcLanguage = intent.getStringExtra("srcLanguage").toString()
        val destLanguage = intent.getStringExtra("destLanguage").toString()

        Toast.makeText(this,srcText, Toast.LENGTH_SHORT).show()
        val isFavourite = intent.getBooleanExtra("isFavourite", false)
        binding.ivSrcImg.setImageResource(Helper.getLanguageFlagResource(this, srcLanguage))
        binding.ivDestImg.setImageResource(Helper.getLanguageFlagResource(this, destLanguage))
        binding.tvSrcText.text = srcText
        binding.tvDestText.text = destText

    }
}