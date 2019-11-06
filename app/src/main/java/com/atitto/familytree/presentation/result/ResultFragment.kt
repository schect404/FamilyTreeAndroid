package com.atitto.familytree.presentation.result

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.atitto.familytree.R
import com.atitto.familytree.base.BaseFragment
import kotlinx.android.synthetic.main.result_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class ResultFragment : BaseFragment() {

    override val localModule = Kodein.Module {
        bind<ResultNavigator>() with provider { ResultNavigatorImpl() }
    }

    override val layoutId = R.layout.result_fragment

    override val navigator: ResultNavigator by kodein.instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt(TITLE_ID_KEY)?.let { tvTitle.setText(it) }
        arguments?.getInt(ANIMATION_ID_KEY)?.let { animationView.setAnimation(it) }
        Handler().postDelayed( { navigator.goToSignIn(tvTitle, animationView) } , 5000)
    }

    companion object {

        private const val TITLE_ID_KEY = "title"
        private const val ANIMATION_ID_KEY = "animation"

        fun newInstanceSignUp() = ResultFragment().apply {
            arguments = Bundle().apply {
                putInt(TITLE_ID_KEY, R.string.success_sign_up)
                putInt(ANIMATION_ID_KEY, R.raw.done_animation)
            }
        }
    }

}
