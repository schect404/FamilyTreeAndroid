package com.atitto.familytree

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

}
