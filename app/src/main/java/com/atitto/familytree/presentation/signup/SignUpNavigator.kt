package com.atitto.familytree.presentation.signup

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.atitto.familytree.R
import com.atitto.familytree.presentation.signin.SignInFragment
import com.atitto.familytree.presentation.signup.SignUpFragment
import com.google.android.material.textfield.TextInputLayout
import java.lang.ref.WeakReference

interface SignUpNavigator {

    fun goToSignIn()

    fun attachFragmentManager(fragmentManager: FragmentManager?)
    fun release()

}

class SignUpNavigatorImpl: SignUpNavigator {

    private var fragmentManager: WeakReference<FragmentManager?> = WeakReference(null)

    override fun attachFragmentManager(fragmentManager: FragmentManager?) {
        this.fragmentManager = WeakReference(fragmentManager)
    }

    override fun release() {
        fragmentManager = WeakReference(null)
    }

    override fun goToSignIn() {
        val previousFragment = fragmentManager.get()?.findFragmentById(R.id.container) ?: return
        val nextFragment = SignInFragment.newInstance()

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

        val sharedIcon = previousFragment.view?.findViewById<ImageView>(R.id.ivTree) ?: return
        val sharedEmail = previousFragment.view?.findViewById<TextInputLayout>(R.id.vEmailInput) ?: return
        val sharedPassword = previousFragment.view?.findViewById<TextInputLayout>(R.id.vPasswordInput) ?: return
        val sharedBtn = previousFragment.view?.findViewById<Button>(R.id.bSignUp) ?: return
        val sharedName = previousFragment.view?.findViewById<TextView>(R.id.tvSignIn) ?: return

        fragmentManager.get()?.beginTransaction()
            ?.replace(R.id.container, nextFragment)
            ?.addSharedElement(sharedBtn, sharedBtn.transitionName)
            ?.addSharedElement(sharedName, sharedName.transitionName)
            ?.addSharedElement(sharedIcon, sharedIcon.transitionName)
            ?.addSharedElement(sharedEmail, sharedEmail.transitionName)
            ?.addSharedElement(sharedPassword, sharedPassword.transitionName)
            ?.commit()
    }

    companion object {
        private const val MOVE_DEFAULT_TIME: Long = 1000
        private const val FADE_DEFAULT_TIME: Long = 300
    }

}