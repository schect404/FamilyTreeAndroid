package com.atitto.familytree.common

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visibleIf(predicate: () -> Boolean) {
    visibility = if(predicate()) View.VISIBLE else View.GONE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.enableIf(predicate: () -> Boolean) {
    isEnabled = predicate()
}