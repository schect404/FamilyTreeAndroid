package com.atitto.familytree.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

abstract class BaseViewModel : ViewModel() {

    protected val disposer = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposer.dispose()
    }

    protected fun <T> Single<T>.getInfo(doOnResult: (T) -> Unit = {}, doOnError: (Throwable) -> Unit = {}, showProgress: Boolean = true) {
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                doOnResult.invoke(it)
            }, {
                doOnError.invoke(it)
            }).also { disposer.add(it) }
    }

    protected fun Completable.makeAction(doOnResult: () -> Unit = {}, doOnError: (Throwable) -> Unit = {}, showProgress: Boolean = true) {
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                doOnResult.invoke()
            }, {
                doOnError.invoke(it)
            }).also { disposer.add(it) }
    }

    protected fun Completable.makeAction(resultLiveData: MutableLiveData<Unit>, errorLiveData: MutableLiveData<String>) {
        makeAction({ resultLiveData.postValue(Unit) }, { errorLiveData.postValue(it.message) })
    }

    protected fun Completable.makeAction(resultLiveData: MutableLiveData<Unit>, doOnError: (Throwable) -> Unit = {}) {
        makeAction({ resultLiveData.postValue(Unit) }, { doOnError.invoke(it) })
    }

    protected fun <T> Observable<T>.subscribeToInfo(doOnResult: (T) -> Unit, doOnError: (Throwable) -> Unit = {}, doOnComplete: () -> Unit = {}, showProgress: Boolean = false) {
        subscribe({
            doOnResult.invoke(it)
        }, {
            doOnError.invoke(it)
            doOnComplete.invoke()
        }, {
            doOnComplete.invoke()
        }).also { disposer.add(it) }
    }

    protected fun <T> Observable<T>.consumeOn(resultLiveData: MutableLiveData<T>, doOnError: (Throwable) -> Unit = {}, doOnComplete: () -> Unit = {}, showProgress: Boolean = false) =
        subscribeToInfo({ resultLiveData.postValue(it) }, doOnError, doOnComplete, showProgress)

    protected fun <T> Single<T>.consumeOn(resultLiveData: MutableLiveData<T>, doOnError: (Throwable) -> Unit = {}, showProgress: Boolean = true) =
        getInfo({ resultLiveData.postValue(it) }, doOnError, showProgress)

}