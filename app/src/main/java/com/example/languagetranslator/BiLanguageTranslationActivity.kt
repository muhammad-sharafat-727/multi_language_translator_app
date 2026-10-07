package com.example.languagetranslator

import android.os.Bundle
import android.text.InputType.TYPE_CLASS_TEXT
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.languagetranslator.adapters.BiLingualConversationAdapter
import com.example.languagetranslator.data.Conversation
import com.example.languagetranslator.data.Helper
import com.example.languagetranslator.data.PreferenceManage
import com.example.languagetranslator.databinding.ActivityBiLanguageTranslationBinding
import com.example.languagetranslator.home.HomeActivity
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class BiLanguageTranslationActivity : AppCompatActivity() {

    private  var srcLanguage : String = "english"
    private  var destLanguage : String = "arabic"
    private var srcId : Int = 12
    private var destId : Int = 2
    private lateinit var conversationAdapter: BiLingualConversationAdapter
    private lateinit var binding: ActivityBiLanguageTranslationBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBiLanguageTranslationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)

        wireBottomNav(
            binding.topNav.root,
            NavTarget.BILANGUAGE,
            homeActivity = HomeActivity::class.java,
            biLanguageTranslationActivity = BiLanguageTranslationActivity::class.java,
            historyActivity = HistoryActivity::class.java,
            starActivity = StarActivity::class.java
        )

        val pref = PreferenceManage(this)

        val conversations = pref.getAllSequenceConversations(srcLanguage,destLanguage)
        updateUI(srcLanguage,destLanguage)


        conversationAdapter = BiLingualConversationAdapter(this , conversations)
        binding.rvStaredConversations.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvStaredConversations.adapter = conversationAdapter
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
                updateUI(srcLanguage,destLanguage)
                val pref = PreferenceManage(this@BiLanguageTranslationActivity)
                conversationAdapter.updateList(pref.getAllSequenceConversations(srcLanguage,destLanguage))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        //dest language dropdown
        binding.destSpinner.adapter = adapter
        binding.destSpinner.setSelection(2)
        binding.destSpinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                destLanguage = Helper.languageArray[position]
                destId = position
                setDestLanguageFlag()
                val pref = PreferenceManage(this@BiLanguageTranslationActivity)
                updateUI(srcLanguage,destLanguage)
                conversationAdapter.updateList(pref.getAllSequenceConversations(srcLanguage,destLanguage))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                if (destLanguage == "")
                {
                    destLanguage = "english"
                }
            }
        }


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


binding.btnClearAll.setOnClickListener {
    pref.clearAllConversationBySrcAndTarget(srcLanguage , destLanguage)
    updateUI(srcLanguage,destLanguage)
    conversationAdapter.updateList(mutableListOf<Conversation>())
}



        binding.messageLeft.setOnClickListener {
            val input = EditText(this)
            input.inputType = TYPE_CLASS_TEXT

            AlertDialog.Builder(this).setTitle("Enter Your Text").setView(input)
                .setPositiveButton("Send"){_,_ ->
                val inputText = input.text.toString()

                if (inputText.isNotEmpty()) {
                    lifecycleScope.launch {
                        val resultText = Helper.translateText(this@BiLanguageTranslationActivity,srcLanguage, destLanguage, inputText)


                        val pref = PreferenceManage(this@BiLanguageTranslationActivity)
                        val conversation = Conversation(
                            inputText,
                            resultText,
                            srcLanguage,
                            destLanguage,
                            false,
                            pref.getSequenceNo(),
                            'l'

                        )

                        pref.saveConverstion(conversation)
                        conversationAdapter.updateList(pref.getAllSequenceConversations(srcLanguage,destLanguage))
                        updateUI(srcLanguage,destLanguage)
                    }
                } else {
                    Toast.makeText(this, "Enter Text First", Toast.LENGTH_SHORT).show()
                }

            }
                    .setNegativeButton("Cancle"){_,_ ->


            }.show()

        }

        binding.messageRight.setOnClickListener {
            val input = EditText(this)
            input.inputType = TYPE_CLASS_TEXT
            AlertDialog.Builder(this).setTitle("Enter Your Text").setView(input).setPositiveButton("Send"){_,_ ->

                val inputText = input.text.toString()

                if (inputText.isNotEmpty()) {
                    lifecycleScope.launch {
                        val resultText = Helper.translateText(this@BiLanguageTranslationActivity,destLanguage, srcLanguage, inputText)


                        val pref = PreferenceManage(this@BiLanguageTranslationActivity)
                        val conversation = Conversation(
                            inputText,
                            resultText,
                            destLanguage,
                            srcLanguage,
                            false,
                            2,
                            'r'
                        )
                        pref.saveConverstion(conversation)
                        conversationAdapter.updateList(pref.getAllSequenceConversations(srcLanguage,destLanguage))
                        updateUI(srcLanguage,destLanguage)
                    }
                } else {
                    Toast.makeText(this, "Enter Text First", Toast.LENGTH_SHORT).show()
                }
            }.setNegativeButton("Cancle"){_,_ ->}.show()


        }



    }
    private fun updateUI(srcLanguage: String, destLanguage: String) {
        val pref = PreferenceManage(this)
        if(pref.anyConversationFound(srcLanguage,destLanguage))
        {
            binding.tvNotDataFound.visibility = View.GONE
            binding.rvStaredConversations.visibility = View.VISIBLE
        }
        else
        {
            binding.tvNotDataFound.visibility = View.VISIBLE
            binding.rvStaredConversations.visibility = View.GONE
        }
    }

    private fun setSrcLanguageFlag() {
        binding.ivSrcImg.setImageResource(Helper.getLanguageFlagResource(this,srcLanguage))

    }
    private fun setDestLanguageFlag() {
        binding.ivDestImg.setImageResource(Helper.getLanguageFlagResource(this,destLanguage))

    }
}