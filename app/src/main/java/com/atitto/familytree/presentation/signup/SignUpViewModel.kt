package com.atitto.familytree.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atitto.domain.auth.AuthUseCase
import com.atitto.domain.auth.Sex
import com.atitto.domain.auth.SignUpModel
import com.atitto.familytree.R
import com.atitto.familytree.base.BaseViewModel
import com.atitto.familytree.common.makeAction
import com.atitto.familytree.common.subscribeWithDebounce
import io.reactivex.disposables.CompositeDisposable
import rx.subjects.PublishSubject
import java.util.*
import java.util.regex.Pattern

abstract class SignUpViewModel : BaseViewModel() {
    abstract val birthDateChangedLiveData: LiveData<Long>
    abstract val sexChangedLiveData: LiveData<Sex>
    abstract val emailErrorLiveData: LiveData<Int>
    abstract val passwordErrorLiveData: LiveData<Int>
    abstract val shouldHandleButtonLiveData: LiveData<Unit>
    abstract val signedUpLiveData: LiveData<Unit>
    abstract val signUpErrorLiveData: LiveData<String>

    abstract fun changeBirthDate(day: Int, month: Int, year: Int)
    abstract fun changeSex(sex: Sex)
    abstract fun changeEmail(email: String)
    abstract fun changePassword(password: String)
    abstract fun listenEmail()
    abstract fun listenPassword()
    abstract fun signUp(data: SignUpModel)
}

class SignUpViewModelImpl(private val authUseCase: AuthUseCase) : SignUpViewModel() {

    override val birthDateChangedLiveData: MutableLiveData<Long> = MutableLiveData()
    override val sexChangedLiveData: MutableLiveData<Sex> = MutableLiveData()
    override val emailErrorLiveData: MutableLiveData<Int> = MutableLiveData()
    override val passwordErrorLiveData: MutableLiveData<Int> = MutableLiveData()
    override val shouldHandleButtonLiveData: MutableLiveData<Unit> = MutableLiveData()
    override val signUpErrorLiveData: MutableLiveData<String> = MutableLiveData()
    override val signedUpLiveData: MutableLiveData<Unit> = MutableLiveData()

    private val emailChangedSubject: PublishSubject<String> = PublishSubject.create()
    private val passwordChangedSubject: PublishSubject<String> = PublishSubject.create()

    override fun changeBirthDate(day: Int, month: Int, year: Int) {
        val date = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }.time
        birthDateChangedLiveData.postValue(date.time)
    }

    override fun changeSex(sex: Sex) = sexChangedLiveData.postValue(sex)

    override fun changeEmail(email: String) = emailChangedSubject.onNext(email)

    override fun listenEmail() {
        emailChangedSubject.subscribeWithDebounce {
            if(isEmailValid(it)) {
                authUseCase.checkEmail(it).makeAction(shouldHandleButtonLiveData) {
                    emailErrorLiveData.postValue(R.string.email_already_exists)
                }
            }
            else emailErrorLiveData.postValue(R.string.email_invalid)
        }
    }

    override fun listenPassword() {
        passwordChangedSubject.subscribeWithDebounce {
            if(it.length < MIN_PASSWORD_CHARS) passwordErrorLiveData.postValue(R.string.password_too_short)
            else { shouldHandleButtonLiveData.postValue(Unit) }
        }
    }

    override fun changePassword(password: String) = passwordChangedSubject.onNext(password)

    override fun signUp(data: SignUpModel) {
        authUseCase.signUp(data).makeAction(signedUpLiveData, signUpErrorLiveData)
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Pattern.compile(EMAIL_PATTERN)
        return pattern.matcher(email).matches()
    }

    companion object {
        private const val MIN_PASSWORD_CHARS = 8
        private const val EMAIL_PATTERN = "^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$"
    }

}
