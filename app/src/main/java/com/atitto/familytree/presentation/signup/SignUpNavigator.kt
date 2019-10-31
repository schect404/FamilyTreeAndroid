package com.atitto.familytree.presentation.signup

import androidx.fragment.app.FragmentManager
import com.atitto.familytree.R
import com.atitto.familytree.presentation.signin.SignInFragment
import com.atitto.familytree.presentation.signup.SignUpFragment
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
        fragmentManager.get()?.beginTransaction()
            ?.replace(R.id.container, SignInFragment.newInstance())
            ?.commitNow()
    }

}