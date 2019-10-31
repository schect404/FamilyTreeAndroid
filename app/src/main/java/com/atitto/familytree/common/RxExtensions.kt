package com.atitto.familytree.common

import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import rx.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit

fun CompositeDisposable.makeAction(action: Completable, errorLiveData: MutableLiveData<String>, postExecute: () -> Unit) {
    add(
        action
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                postExecute.invoke()
            },{
                errorLiveData.postValue(it.message)
            })
    )
}

fun <T> CompositeDisposable.makeAction(action: Flowable<T>, errorLiveData: MutableLiveData<String>, postExecute: (T) -> Unit) {
    add(
        action
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                postExecute.invoke(it)
            }, {
                errorLiveData.postValue(it.message)
            })
    )
}

fun <T> CompositeDisposable.makeAction(action: Observable<T>, errorExecute: (Throwable) -> Unit, postExecute: (T) -> Unit) {
    add(
        action
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                postExecute.invoke(it)
            }, {
                errorExecute.invoke(it)
            })
    )
}

fun <T> CompositeDisposable.makeAction(action: Single<T>, errorLiveData: MutableLiveData<String>, postExecute: (T) -> Unit) {
    add(
        action
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                postExecute.invoke(it)
            }, {
                if(it is NoSuchElementException) errorLiveData.postValue("Could not load weather for this city")
                else errorLiveData.postValue(it.message)
            })
    )
}

fun <T> CompositeDisposable.makeAction(action: Single<T>, errorExecute: (Throwable) -> Unit, postExecute: (T) -> Unit) {
    add(
        action
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                postExecute.invoke(it)
            }, {
                errorExecute.invoke(it)
            })
    )
}

fun CompositeDisposable.makeAction(action: Completable, errorExecute: (Throwable) -> Unit, postExecute: () -> Unit) {
    add(
        action
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                postExecute.invoke()
            }, {
                errorExecute.invoke(it)
            })
    )
}

fun <T> PublishSubject<T>.subscribeWithDebounce(postExecute: (T) -> Unit) {
            debounce(500, TimeUnit.MILLISECONDS)
            .subscribe { postExecute.invoke(it) }
}