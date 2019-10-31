package com.atitto.familytree.presentation.signin

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.transition.Fade
import com.atitto.familytree.R
import com.atitto.familytree.presentation.signup.SignUpFragment
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.google.android.material.textfield.TextInputLayout

interface SignInNavigator {

    fun goToSignUp()

}

class SignInNavigatorImpl(private  val fragmentManager: FragmentManager?): SignInNavigator {

    override fun goToSignUp() {

        val previousFragment = fragmentManager?.findFragmentById(R.id.container) ?: return
        val nextFragment = SignUpFragment.newInstance()

        val exitFade = Fade()
        exitFade.duration = FADE_DEFAULT_TIME
        previousFragment.exitTransition = exitFade

        val context = previousFragment.requireContext()

        previousFragment.sharedElementReturnTransition = TransitionSet().apply {
            addTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.move))
            duration = MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME
        }

        val enterFade = Fade()
        enterFade.duration = FADE_DEFAULT_TIME
        nextFragment.enterTransition = enterFade

        nextFragment.sharedElementEnterTransition = TransitionSet().apply {
            addTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.move))
            duration = MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME
        }

        val sharedIcon = previousFragment.view?.findViewById<ImageView>(R.id.ivTree_1) ?: return
        val sharedEmail = previousFragment.view?.findViewById<TextInputLayout>(R.id.vEmailInput) ?: return
        val sharedPassword = previousFragment.view?.findViewById<TextInputLayout>(R.id.vPasswordInput) ?: return
        val sharedBtn = previousFragment.view?.findViewById<Button>(R.id.bSignIn) ?: return
        val sharedName = previousFragment.view?.findViewById<TextView>(R.id.tvSignIn) ?: return

        fragmentManager.beginTransaction()
            .replace(R.id.container, nextFragment)
            .addSharedElement(sharedBtn, sharedBtn.transitionName)
            .addSharedElement(sharedName, sharedName.transitionName)
            .addSharedElement(sharedIcon, sharedIcon.transitionName)
            .addSharedElement(sharedEmail, sharedEmail.transitionName)
            .addSharedElement(sharedPassword, sharedPassword.transitionName)
            .commit()
    }

    companion object {
        private const val MOVE_DEFAULT_TIME: Long = 1000
        private const val FADE_DEFAULT_TIME: Long = 300
    }

}