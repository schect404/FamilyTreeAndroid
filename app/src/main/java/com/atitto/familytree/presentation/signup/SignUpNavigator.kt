package com.atitto.familytree.presentation.signup

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.atitto.familytree.R
import com.atitto.familytree.base.BaseNavigator
import com.atitto.familytree.common.goWithAnimation
import com.atitto.familytree.presentation.result.ResultFragment
import com.atitto.familytree.presentation.signin.SignInFragment
import com.atitto.familytree.presentation.signup.SignUpFragment
import com.google.android.material.textfield.TextInputLayout
import java.lang.ref.WeakReference

interface SignUpNavigator : BaseNavigator {

    fun goToSignIn(vararg sharedViews: View)

    fun goToSuccess(vararg sharedViews: View)

}

class SignUpNavigatorImpl: SignUpNavigator {

    override var fragmentManager: WeakReference<FragmentManager?> = WeakReference(null)

    override fun goToSignIn(vararg sharedViews: View) {
        fragmentManager.get()?.goWithAnimation(SignInFragment.newInstance(), *sharedViews)
    }

    override fun goToSuccess(vararg sharedViews: View) {
        fragmentManager.get()?.goWithAnimation(ResultFragment.newInstanceSignUp(), *sharedViews)
    }

}