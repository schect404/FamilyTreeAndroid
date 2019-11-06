package com.atitto.familytree.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    protected val disposer = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposer.dispose()
    }

}