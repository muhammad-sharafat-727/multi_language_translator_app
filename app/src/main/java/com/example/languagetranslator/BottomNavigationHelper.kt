package com.example.languagetranslator

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.graphics.toColorInt

enum class NavTarget { HOME, HISTORY, STAR, BILANGUAGE }

fun Activity.wireBottomNav(
    navRoot: View,
    current: NavTarget,
    homeActivity: Class<*>,
    historyActivity: Class<*>,
    starActivity: Class<*>,
    biLanguageTranslationActivity: Class<*>,
) {
    applyNavigationColors(navRoot, current)

    navRoot.findViewById<View>(R.id.nav_home).setOnClickListener {
        if (current != NavTarget.HOME) {
            startActivity(Intent(this, homeActivity))
            finish()
        }
    }
    navRoot.findViewById<View>(R.id.nav_conversation).setOnClickListener {
        if (current != NavTarget.BILANGUAGE) {
            startActivity(Intent(this, biLanguageTranslationActivity))
            finish()
        }
    }
    navRoot.findViewById<View>(R.id.nav_history).setOnClickListener {
        if (current != NavTarget.HISTORY) {
            startActivity(Intent(this, historyActivity))
            finish()
        }
    }
    navRoot.findViewById<View>(R.id.nav_favourate).setOnClickListener {
        if (current != NavTarget.STAR) {
            startActivity(Intent(this, starActivity))
            finish()
        }
    }
}



fun applyNavigationColors(navRoot: View, current: NavTarget) {
    val selectedColor = "#0000FF".toColorInt()
    val transparentColor = "#00000000".toColorInt()

    val homeBtn = navRoot.findViewById<View>(R.id.nav_home_bar)
    val conversationBtn = navRoot.findViewById<View>(R.id.nav_conversation_bar)
    val historyBtn = navRoot.findViewById<View>(R.id.nav_history_bar)
    val favouriteBtn = navRoot.findViewById<View>(R.id.nav_favourate_bar)



    homeBtn.setBackgroundColor(if (current == NavTarget.HOME) selectedColor else transparentColor)
    conversationBtn.setBackgroundColor(if (current == NavTarget.BILANGUAGE) selectedColor else transparentColor)
    historyBtn.setBackgroundColor(if (current == NavTarget.HISTORY) selectedColor else transparentColor)
    favouriteBtn.setBackgroundColor(if (current == NavTarget.STAR) selectedColor else transparentColor)

}

