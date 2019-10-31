package com.atitto.familytree.presentation.signin

import androidx.fragment.app.FragmentManager
import com.atitto.familytree.R
import com.atitto.familytree.presentation.signup.SignUpFragment

interface SignInNavigator {

    fun goToSignUp()

}

class SignInNavigatorImpl(private  val fragmentManager: FragmentManager?): SignInNavigator {

    override fun goToSignUp() {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.container, SignUpFragment.newInstance())
            ?.commitNow()
    }

}