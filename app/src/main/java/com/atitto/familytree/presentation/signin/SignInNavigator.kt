package com.atitto.familytree.presentation.signin

import android.view.View
import androidx.fragment.app.FragmentManager
import com.atitto.familytree.base.BaseNavigator
import com.atitto.familytree.common.goWithAnimation
import com.atitto.familytree.presentation.signup.SignUpFragment
import java.lang.ref.WeakReference

interface SignInNavigator : BaseNavigator {

    fun goToSignUp(vararg sharedViews: View)

}

class SignInNavigatorImpl : SignInNavigator {

    override var fragmentManager: WeakReference<FragmentManager?> = WeakReference(null)

    override fun goToSignUp(vararg sharedViews: View) {
        fragmentManager.get()?.goWithAnimation(SignUpFragment.newInstance(), *sharedViews)
    }
}