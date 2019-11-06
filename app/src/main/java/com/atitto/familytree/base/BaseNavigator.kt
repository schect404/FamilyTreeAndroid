package com.atitto.familytree.base

import androidx.fragment.app.FragmentManager
import java.lang.ref.WeakReference

interface BaseNavigator {

    var fragmentManager: WeakReference<FragmentManager?>

    fun attachFragmentManager(fragmentManager: FragmentManager?) {
        this.fragmentManager = WeakReference(fragmentManager)
    }

    fun release() {
        fragmentManager = WeakReference(null)
    }

}