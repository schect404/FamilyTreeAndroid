package com.atitto.familytree.presentation.result

import android.view.View
import androidx.fragment.app.FragmentManager
import com.atitto.familytree.base.BaseNavigator
import com.atitto.familytree.common.goWithAnimation
import com.atitto.familytree.presentation.signin.SignInFragment
import java.lang.ref.WeakReference

interface ResultNavigator : BaseNavigator {

    fun goToSignIn(vararg sharedViews: View)

}

class ResultNavigatorImpl: ResultNavigator {

    override var fragmentManager: WeakReference<FragmentManager?> = WeakReference(null)

    override fun goToSignIn(vararg sharedViews: View) {
        fragmentManager.get()?.goWithAnimation(SignInFragment.newInstance(), *sharedViews)
    }

}