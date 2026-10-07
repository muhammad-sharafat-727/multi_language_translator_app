package com.example.languagetranslator.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.languagetranslator.BiLanguageTranslationActivity
import com.example.languagetranslator.HistoryActivity
import com.example.languagetranslator.NavTarget
import com.example.languagetranslator.R
import com.example.languagetranslator.StarActivity
import com.example.languagetranslator.data.Conversation
import com.example.languagetranslator.data.Helper
import com.example.languagetranslator.data.PreferenceManage
import com.example.languagetranslator.databinding.ActivityHomeBinding
import com.example.languagetranslator.wireBottomNav
import kotlinx.coroutines.launch
import java.util.Locale

open class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private  var srcLanguage : String = "english"
    private  var destLanguage : String = "arabic"
    private var srcId : Int = 12
    private var destId : Int = 2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


         val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, Helper.languageArray)

        //src language dropdown
        binding.srcSpinner.adapter = adapter
        binding.srcSpinner.setSelection(11)
        binding.srcSpinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
               srcLanguage = Helper.languageArray[position]
                srcId = position
                setSrcLanguageFlag()
            }


            override fun onNothingSelected(p0: AdapterView<*>?) {
                if (srcLanguage == "")
                {
                    srcLanguage = Helper.languageArray[2]
                }
            }
        }

        //dest language dropdown
        binding.destSpinner.adapter = adapter
        binding.destSpinner.setSelection(2)
        binding.destSpinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                destLanguage = Helper.languageArray[position]
                binding.tvDestLanguage.text = destLanguage
                destId = position
                setDestLanguageFlag()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                if (destLanguage == "")
                {
                    destLanguage = "english"
                }
            }
        }
        binding.btnSidebar.setOnClickListener {
//            showSideBar()
        }
        binding.tvDestLanguage.text = destLanguage
        binding.btnExchangeLanguage.setOnClickListener {
            binding.destSpinner.setSelection(srcId)
            binding.srcSpinner.setSelection(destId)
            val temp = srcLanguage
            srcLanguage = destLanguage
            destLanguage = temp
            Toast.makeText(this,"$srcLanguage     ......    $destLanguage",Toast.LENGTH_SHORT).show()
            setSrcLanguageFlag()
            setDestLanguageFlag()
        }



        wireBottomNav(
            binding.topNav.root,
            NavTarget.HOME,
            homeActivity = HomeActivity::class.java,
            biLanguageTranslationActivity = BiLanguageTranslationActivity::class.java,
            historyActivity = HistoryActivity::class.java,
            starActivity = StarActivity::class.java
        )

        binding.btnTranslate.setOnClickListener {
            val inputText = binding.etTextToTranslate.text.toString().trim()

            if (inputText.isNotEmpty()) {
                lifecycleScope.launch {
                    val resultText = Helper.translateText(this@HomeActivity,srcLanguage, destLanguage, inputText)

                    binding.tvTargetText.text = resultText


                    val conversation = Conversation(
                        inputText,
                        resultText,
                        srcLanguage,
                        destLanguage
                    )
                    val pref = PreferenceManage(this@HomeActivity)
                    pref.saveConverstion(conversation)
                }
            } else {
                Toast.makeText(this, "Enter Text First", Toast.LENGTH_SHORT).show()
            }
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)


        binding.btnStar.setOnClickListener {
            if (binding.tvTargetText.text.isNotBlank())
            {
                Toast.makeText(this,"Marked as Favourite!", Toast.LENGTH_SHORT).show()

                val pref = PreferenceManage(this@HomeActivity)
                pref.markAsFavourite()
            }
            else
            {
                Toast.makeText(this,"Empty! First Translate", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnShare.setOnClickListener {
            if(binding.tvTargetText.text.isNotEmpty())
            {
                val shareText = "Src Text : ${binding.etTextToTranslate.text}\n\nTranslated Text : ${binding.tvTargetText.text}"

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }

                startActivity(Intent.createChooser(intent, "Share via"))

            }
            else
            {
                Toast.makeText(this,"Translate Text First!", Toast.LENGTH_SHORT).show()
            }
            }

        binding.btnCopy.setOnClickListener { view ->

            if(binding.tvTargetText.text.isNotEmpty())
            {
            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(
                "label",
                "Src Text : ${binding.etTextToTranslate.text}\n\nTranslated Text : ${binding.tvTargetText.text}"
            )
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(this, "Text copied to clipboard!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this,"Translate Text First!", Toast.LENGTH_SHORT).show()

            }
        }


    }


    private fun setSrcLanguageFlag() {
        binding.ivSrcImg.setImageResource(Helper.getLanguageFlagResource(this,srcLanguage))

    }
    private fun setDestLanguageFlag() {
        binding.ivDestImg.setImageResource(Helper.getLanguageFlagResource(this,destLanguage))

    }

}


