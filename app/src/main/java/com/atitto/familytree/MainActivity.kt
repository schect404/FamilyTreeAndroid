package com.atitto.familytree

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.atitto.familytree.presentation.signin.SignInFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}
