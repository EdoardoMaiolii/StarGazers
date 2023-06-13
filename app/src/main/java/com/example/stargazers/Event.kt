package com.example.stargazers

class Event<out T>(private val content: T) {

    var handled = false
        private set // Allow external read but not write

    fun getContentIfNotHandled(): T? {
        return if (handled) {
            null
        } else {
            handled = true
            content
        }
    }
}