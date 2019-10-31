package com.atitto.familytree.presentation.signin

import android.os.Bundle
import android.view.View
import com.atitto.familytree.R
import com.atitto.familytree.base.BaseFragment
import com.atitto.familytree.common.enableIf
import kotlinx.android.synthetic.main.main_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.util.regex.Pattern

class SignInFragment : BaseFragment() {

    override val localModule = Kodein.Module {
        bind<MainViewModel>() with provider { MainViewModelImpl(instance()) }
        bind<SignInNavigator>() with provider { SignInNavigatorImpl(fragmentManager) }
    }

    override val layoutId = R.layout.main_fragment

    private val viewModel: MainViewModel by kodein.instance()

    private val navigator: SignInNavigator by kodein.instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bSignUp.setOnClickListener { navigator.goToSignUp() }
        etPassword.textChangedListener { handleButtonEnable() }
        etEmail.textChangedListener { handleButtonEnable() }
    }

    private fun handleButtonEnable() {
        val pattern = Pattern.compile(EMAIL_PATTERN)
        bSignIn.enableIf { (etPassword.text.toString().length >= 8).and(pattern.matcher(etEmail.text.toString()).matches()) }
    }

    companion object {

        private const val EMAIL_PATTERN = "^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$"

        fun newInstance() = SignInFragment()
    }

}
