package com.atitto.familytree.common

import androidx.databinding.ObservableList

fun <T> ObservableList<T>.loaded(list: List<T>) {
    clear()
    addAll(list)
}