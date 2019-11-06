package com.atitto.familytree.common

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.atitto.familytree.R

private const val MOVE_DEFAULT_TIME: Long = 700
private const val FADE_DEFAULT_TIME: Long = 100

fun Fragment.animateExit() {
    val exitFade = Fade()
    exitFade.duration = MOVE_DEFAULT_TIME
    exitTransition = exitFade
}

fun Fragment.animateEnter() {
    val enterFade = Fade()
    enterFade.duration = MOVE_DEFAULT_TIME
    enterFade.startDelay = FADE_DEFAULT_TIME
    enterTransition = enterFade
}

fun Fragment.animateShared(context: Context) {
    sharedElementEnterTransition = TransitionSet().apply {
        addTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.move))
        duration = MOVE_DEFAULT_TIME
    }
}

fun FragmentManager.goWithAnimation(targetFragment: Fragment, vararg sharedViews: View) {

    val previousFragment = findFragmentById(R.id.container) ?: return
    previousFragment.animateExit()

    targetFragment.animateShared(previousFragment.requireContext())
    //targetFragment.animateEnter()

    beginTransaction()
    .replace(R.id.container, targetFragment)
    .apply { sharedViews.forEach { addSharedElement(it, it.transitionName) } }
    .commit()

}