package com.atitto.familytree.presentation.signup

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.atitto.domain.auth.Sex
import com.atitto.domain.auth.SignUpModel
import com.atitto.familytree.R
import com.atitto.familytree.base.BaseFragment
import com.atitto.familytree.common.bindDataTo
import com.atitto.familytree.common.gone
import com.atitto.familytree.common.toSeconds
import com.atitto.familytree.common.visible
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
        bind<SignUpViewModel>() with provider { SignUpViewModelImpl(instance()) }
        bind<SignUpNavigator>() with provider { SignUpNavigatorImpl() }
    }

    override val layoutId: Int = R.layout.sign_up_fragment

    private val navigator: SignUpNavigator by kodein.instance()

    private val viewModel: SignUpViewModel by kodein.instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bSignIn.setOnClickListener { navigator.goToSignIn() }
        registerFieldClicks()
        registerSignUpListener()
        registerTextListeners()
        bindViewModel()
        navigator.attachFragmentManager(fragmentManager)
    }

    private fun bindViewModel() {
        viewModel.listenEmail()
        viewModel.listenPassword()
        bindDataTo(viewModel.birthDateChangedLiveData) { actionWithButonHandling { dpBirth.setText(DateFormatter.defaultDateFormat.format(Date(it))) } }
        bindDataTo(viewModel.sexChangedLiveData) { actionWithButonHandling { etSex.setText(it.title) } }
        bindDataTo(viewModel.emailErrorLiveData) { actionWithButonHandling { etEmail.error = context().resources.getString(it) } }
        bindDataTo(viewModel.passwordErrorLiveData) { actionWithButonHandling { etPassword.error = context().resources.getString(it) } }
        bindDataTo(viewModel.shouldHandleButtonLiveData) { handleButtonAvailable() }
        bindDataTo(viewModel.signUpErrorLiveData) {
            bSignUp.isEnabled = true
            pbSigning.gone()
            Toast.makeText(context(), it, Toast.LENGTH_SHORT).show()
        }
        bindDataTo(viewModel.signedUpLiveData) {
            pbSigning.gone()
            Toast.makeText(context(), "Signed up", Toast.LENGTH_SHORT).show()
            navigator.goToSignIn()
        }
    }

    private fun actionWithButonHandling(action: () -> Unit) {
        action.invoke()
        handleButtonAvailable()
    }

    private fun registerTextListeners() {
        etEmail.textChangedListener {
            etEmail.error = null
            viewModel.changeEmail(it)
        }

        etPassword.textChangedListener {
            etPassword.error = null
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
            bSignUp.isEnabled = false
        }
    }

    private fun handleButtonAvailable() {
        val isEmailValid = (!etEmail.text.isNullOrEmpty()).and(etEmail.error.isNullOrEmpty())
        val isPasswordValid = (!etPassword.text.isNullOrEmpty()).and(etPassword.error.isNullOrEmpty())
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