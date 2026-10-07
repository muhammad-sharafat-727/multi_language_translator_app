package com.example.languagetranslator.data

import android.content.Context
import android.widget.Toast
import com.example.languagetranslator.R
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object Helper {

    fun getMLKitLanguageCode(languageName: String): String {
        return when (languageName.lowercase().trim()) {
            "afrikaans" -> TranslateLanguage.AFRIKAANS
            "albanian" -> TranslateLanguage.ALBANIAN
            "arabic" -> TranslateLanguage.ARABIC
            "belarusian" -> TranslateLanguage.BELARUSIAN
            "bengali" -> TranslateLanguage.BENGALI
            "bulgarian" -> TranslateLanguage.BULGARIAN
            "catalan" -> TranslateLanguage.CATALAN
            "chinese" -> TranslateLanguage.CHINESE
            "croatian" -> TranslateLanguage.CROATIAN
            "czech" -> TranslateLanguage.CZECH
            "danish" -> TranslateLanguage.DANISH
            "english" -> TranslateLanguage.ENGLISH
            "esperanto" -> TranslateLanguage.ESPERANTO
            "estonian" -> TranslateLanguage.ESTONIAN
            "finnish" -> TranslateLanguage.FINNISH
            "french" -> TranslateLanguage.FRENCH
            "galician" -> TranslateLanguage.GALICIAN
            "georgian" -> TranslateLanguage.GEORGIAN
            "german" -> TranslateLanguage.GERMAN
            "greek" -> TranslateLanguage.GREEK
            "haitian creole" -> TranslateLanguage.HAITIAN_CREOLE
            "hebrew" -> TranslateLanguage.HEBREW
            "hindi" -> TranslateLanguage.HINDI
            "hungarian" -> TranslateLanguage.HUNGARIAN
            "icelandic" -> TranslateLanguage.ICELANDIC
            "indonesian" -> TranslateLanguage.INDONESIAN
            "irish" -> TranslateLanguage.IRISH
            "italian" -> TranslateLanguage.ITALIAN
            "japanese" -> TranslateLanguage.JAPANESE
            "kannada" -> TranslateLanguage.KANNADA
            "korean" -> TranslateLanguage.KOREAN
            "latvian" -> TranslateLanguage.LATVIAN
            "lithuanian" -> TranslateLanguage.LITHUANIAN
            "macedonian" -> TranslateLanguage.MACEDONIAN
            "malay" -> TranslateLanguage.MALAY
            "norwegian" -> TranslateLanguage.NORWEGIAN
            "persian" -> TranslateLanguage.PERSIAN
            "polish" -> TranslateLanguage.POLISH
            "portuguese" -> TranslateLanguage.PORTUGUESE
            "romanian" -> TranslateLanguage.ROMANIAN
            "russian" -> TranslateLanguage.RUSSIAN
            "slovak" -> TranslateLanguage.SLOVAK
            "slovenian" -> TranslateLanguage.SLOVENIAN
            "spanish" -> TranslateLanguage.SPANISH
            "swedish" -> TranslateLanguage.SWEDISH
            "thai" -> TranslateLanguage.THAI
            "turkish" -> TranslateLanguage.TURKISH
            "ukrainian" -> TranslateLanguage.UKRAINIAN
            "urdu" -> TranslateLanguage.URDU
            "vietnamese" -> TranslateLanguage.VIETNAMESE
            "welsh" -> TranslateLanguage.WELSH
            else -> TranslateLanguage.ENGLISH
        }
    }


val languageArray = arrayOf(
    "afrikaans", "albanian", "arabic", "belarusian", "bengali",
    "bulgarian", "catalan", "chinese", "croatian", "czech",
    "danish",  "english", "esperanto", "estonian",
    "finnish", "french", "galician", "georgian", "german",
    "greek", "haitian creole", "hebrew", "hindi", "hungarian",
    "icelandic", "indonesian", "irish", "italian", "japanese",
    "kannada", "korean", "latvian", "lithuanian", "macedonian",
    "malay", "norwegian", "persian", "polish", "portuguese",
    "romanian", "russian", "slovak", "slovenian", "spanish",
    "swedish", "thai", "turkish", "ukrainian", "urdu",
    "vietnamese", "welsh"
)




    fun getLanguageFlagResource(context: Context, languageName: String): Int {
        val drawableName = "ic_flag_$languageName"
        val resId = context.resources.getIdentifier(drawableName, "drawable", context.packageName)
        return if (resId != 0) resId else R.drawable.ic_flag_english
    }


    suspend fun translateText(context : Context , sourceLang: String, destLang: String, textToTranslate: String): String {
        val sourceLangCode = getMLKitLanguageCode(sourceLang)
        val destLangCode =getMLKitLanguageCode(destLang)

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLangCode)
            .setTargetLanguage(destLangCode)
            .build()

        val translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        Toast.makeText(context.applicationContext, "Preparing language models...", Toast.LENGTH_SHORT).show()

        return suspendCancellableCoroutine { continuation ->
            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    translator.translate(textToTranslate)
                        .addOnSuccessListener { translatedResult ->
                            translator.close()
                            if (continuation.isActive) continuation.resume(translatedResult)
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(context.applicationContext, "Translation failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                            translator.close()
                            if (continuation.isActive) continuation.resume("")
                        }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context.applicationContext, "Model download failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                    translator.close()
                    if (continuation.isActive) continuation.resume("")
                }
        }
    }


}