package com.atitto.datashared

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}