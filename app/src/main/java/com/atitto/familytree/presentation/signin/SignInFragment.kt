package com.atitto.familytree.presentation.signin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.atitto.familytree.R
import com.atitto.familytree.base.BaseFragment
import com.atitto.familytree.common.*
import kotlinx.android.synthetic.main.main_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.util.regex.Pattern

class SignInFragment : BaseFragment() {

    override val localModule = Kodein.Module {
        bind<SignInViewModel>() with provider { MainViewModelImpl(instance()) }
        bind<SignInNavigator>() with provider { SignInNavigatorImpl() }
    }

    override val layoutId = R.layout.main_fragment

    private val viewModel: SignInViewModel by kodein.instance()

    override val navigator: SignInNavigator by kodein.instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bSignUp.setOnClickListener { navigator.goToSignUp(vEmailInput, vPasswordInput, bSignIn, ivTree_1, tvSignIn) }
        bSignIn.setOnClickListener {
            bSignIn.disable()
            pbSigning.visible()
            viewModel.signIn(etEmail.text?.toString() ?: "", etPassword.text?.toString() ?: "")
        }
        etPassword.textChangedListener { handleButtonEnable() }
        etEmail.textChangedListener { handleButtonEnable() }
        bindViewModel()
    }

    private fun handleButtonEnable() {
        vEmailInput.error = null
        vPasswordInput.error = null
        val pattern = Pattern.compile(EMAIL_PATTERN)
        bSignIn.enableIf { (etPassword.text.toString().length >= 8).and(pattern.matcher(etEmail.text.toString()).matches()) }
    }

    private fun bindViewModel() {
        bindDataTo(viewModel.successfulSignInLiveData) { onResultAction { Toast.makeText(context(), it, Toast.LENGTH_LONG).show() } }
        bindDataTo(viewModel.emailErrorLiveData) { onResultAction { vEmailInput.error = resources.getString(R.string.email_not_found) } }
        bindDataTo(viewModel.passwordErrorLiveData) { onResultAction { vPasswordInput.error = resources.getString(R.string.invalid_password) } }
    }

    private fun onResultAction(action: () -> Unit) {
        bSignIn.enable()
        pbSigning.gone()
        action.invoke()
    }

    companion object {

        private const val EMAIL_PATTERN = "^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$"

        fun newInstance() = SignInFragment()
    }

}
