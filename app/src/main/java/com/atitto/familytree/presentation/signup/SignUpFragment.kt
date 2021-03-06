package com.atitto.familytree.presentation.signup

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.transition.TransitionInflater
import com.atitto.domain.auth.Sex
import com.atitto.domain.auth.SignUpModel
import com.atitto.familytree.R
import com.atitto.familytree.base.BaseFragment
import com.atitto.familytree.common.*
import com.atitto.familytree.helpers.DateFormatter
import com.atitto.familytree.helpers.DialogHelper
import kotlinx.android.synthetic.main.sign_up_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.util.*

class SignUpFragment : BaseFragment() {

    override val localModule = Kodein.Module {
        bind<SignUpViewModel>() with provider { provideViewModelWithFactory { SignUpViewModelImpl(instance()) } }
        bind<SignUpNavigator>() with provider { SignUpNavigatorImpl() }
    }

    override val layoutId: Int = R.layout.sign_up_fragment

    override val navigator: SignUpNavigator by kodein.instance()

    private val viewModel: SignUpViewModel by kodein.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bSignIn.setOnClickListener { navigator.goToSignIn(ivTree, tvSignIn, vEmailInput, vPasswordInput, bSignUp) }
        registerFieldClicks()
        registerSignUpListener()
        registerTextListeners()
        viewModel.listenEmail()
        viewModel.listenPassword()
    }

    private fun bindViewModel() {
        bindDataTo(viewModel.birthDateChangedLiveData) { actionWithButtonHandling { dpBirth.setText(DateFormatter.defaultDateFormat.format(Date(it))) } }
        bindDataTo(viewModel.sexChangedLiveData) { actionWithButtonHandling { etSex.setText(it.title) } }
        bindDataTo(viewModel.emailErrorLiveData) { actionWithButtonHandling { vEmailInput.error = context().resources.getString(it) } }
        bindDataTo(viewModel.passwordErrorLiveData) { actionWithButtonHandling { vPasswordInput.error = context().resources.getString(it) } }
        bindDataTo(viewModel.shouldHandleButtonLiveData) { handleButtonAvailable() }
        bindDataTo(viewModel.signUpErrorLiveData) {
            bSignUp.enable()
            pbSigning.gone()
            Toast.makeText(context(), it, Toast.LENGTH_SHORT).show()
        }
        bindDataTo(viewModel.signedUpLiveData) {
            pbSigning.gone()
            navigator.goToSuccess(bSignUp, tvSignIn)
        }
    }

    private fun actionWithButtonHandling(action: () -> Unit) {
        action.invoke()
        handleButtonAvailable()
    }

    private fun registerTextListeners() {
        etEmail.textChangedListener {
            vEmailInput.error = null
            viewModel.changeEmail(it)
        }

        etPassword.textChangedListener {
            vPasswordInput.error = null
            viewModel.changePassword(it)
        }
        etFirstName.textChangedListener { handleButtonAvailable() }
        etLastName.textChangedListener { handleButtonAvailable() }
    }

    private fun registerFieldClicks() {
        dpBirth.setOnClickListener {
            DialogHelper.calendarDialogPast(context(),
                calendar = Calendar.getInstance().apply { viewModel.birthDateChangedLiveData.value?.let { timeInMillis = it } },
                listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    viewModel.changeBirthDate(dayOfMonth, month, year)
                })
        }
        etSex.setOnClickListener {
            val currentlyChosen = viewModel.sexChangedLiveData.value?.let { Sex.values().indexOf(it) } ?: 0
            DialogHelper.sexDialogChoose(context(), currentlyChosen = currentlyChosen) {
                viewModel.changeSex(it)
            }
        }
    }

    private fun registerSignUpListener() {
        bSignUp.setOnClickListener {
            val birthDate = viewModel.birthDateChangedLiveData.value ?: return@setOnClickListener
            val sex = viewModel.sexChangedLiveData.value ?: return@setOnClickListener
            viewModel.signUp(
                SignUpModel(
                    email = etEmail.text.toString(),
                    firstName = etFirstName.text.toString(),
                    lastName = etLastName.text.toString(),
                    birthDate = birthDate.toSeconds(),
                    sex = sex,
                    password = etPassword.text.toString()
                )
            )
            pbSigning.visible()
            bSignUp.disable()
        }
    }

    private fun handleButtonAvailable() {
        val isEmailValid = (!etEmail.text.isNullOrEmpty()).and(vEmailInput.error.isNullOrEmpty())
        val isPasswordValid = (!etPassword.text.isNullOrEmpty()).and(vPasswordInput.error.isNullOrEmpty())
        val areNamesValid = (!etFirstName.text.isNullOrEmpty()).and(!etLastName.text.isNullOrEmpty())
        val isOtherDataValid = (!dpBirth.text.isNullOrEmpty()).and(!etSex.text.isNullOrEmpty())
        bSignUp.isEnabled = isEmailValid.and(isPasswordValid).and(areNamesValid).and(isOtherDataValid)
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator.release()
    }

    companion object {
        fun newInstance() = SignUpFragment()
    }

}