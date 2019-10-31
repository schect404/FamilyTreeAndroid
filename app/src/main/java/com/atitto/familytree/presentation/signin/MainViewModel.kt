package com.atitto.familytree.presentation.signin

import androidx.lifecycle.ViewModel
import com.atitto.domain.auth.AuthUseCase

interface MainViewModel {

}

class MainViewModelImpl(private val authUseCase: AuthUseCase): ViewModel(), MainViewModel {

}
