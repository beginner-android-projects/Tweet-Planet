package com.hsmsample.tweetplanet

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class LiveDataTestObserver<T> : Observer<T> {

    val observedValues = mutableListOf<T>()

    override fun onChanged(value: T) {
        observedValues.add(value)
    }
}

fun <T> LiveData<T>.test() = LiveDataTestObserver<T>()
    .also { observeForever(it) }
