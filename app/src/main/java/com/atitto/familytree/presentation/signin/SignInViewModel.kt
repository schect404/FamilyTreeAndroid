package com.atitto.familytree.presentation.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.atitto.domain.auth.AuthUseCase
import com.atitto.familytree.base.BaseViewModel
import com.atitto.familytree.common.makeAction
import com.google.gson.JsonParser
import retrofit2.HttpException

interface SignInViewModel {
    val successfulSignInLiveData: LiveData<String>
    val errorSignInLiveData: LiveData<Unit>
    val emailErrorLiveData: LiveData<Unit>
    val passwordErrorLiveData: LiveData<Unit>

    fun signIn(email: String, password: String)
}

class MainViewModelImpl(private val authUseCase: AuthUseCase): BaseViewModel(), SignInViewModel {

    override val successfulSignInLiveData: MutableLiveData<String> = MutableLiveData()
    override val errorSignInLiveData: MutableLiveData<Unit> = MutableLiveData()
    override val emailErrorLiveData: MutableLiveData<Unit> = MutableLiveData()
    override val passwordErrorLiveData: MutableLiveData<Unit> = MutableLiveData()

    override fun signIn(email: String, password: String) {
        disposer.makeAction(authUseCase.signIn(email, password), ::handleError) {
            authUseCase.storeTokens(it)
            successfulSignInLiveData.postValue(it.userId)
        }
    }

    private fun handleError(error: Throwable) {
        val errorBody = (error as? com.jakewharton.retrofit2.adapter.rxjava2.HttpException)?.response()?.errorBody()?.string() ?: return
        val errorData = JsonParser().parse(errorBody).asJsonObject
        if(errorData["error"].asJsonObject["message"].asString == EMAIL_ERROR) emailErrorLiveData.postValue(Unit)
        else passwordErrorLiveData.postValue(Unit)
    }

    companion object {
        private const val EMAIL_ERROR = "EMAIL_NOT_FOUND"
    }

}
