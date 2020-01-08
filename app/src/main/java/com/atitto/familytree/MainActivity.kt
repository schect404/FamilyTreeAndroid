package com.atitto.familytree

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.atitto.familytree.base.App
import com.atitto.familytree.presentation.signin.SignInFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> theme.applyStyle(R.style.AppTheme_Light, true)
            else -> theme.applyStyle(R.style.AppTheme_Dark, true)
        }
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SignInFragment.newInstance())
                .commit()
        }
        supportActionBar?.hide()
        val token = getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("access_token", null)
        if (token != null)
            Toast.makeText(this, token, Toast.LENGTH_LONG).show()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        recreate()
        super.onConfigurationChanged(newConfig)
    }
}
