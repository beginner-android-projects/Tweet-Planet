package com.hsmsample.tweetplanet.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showLongToast(message: String): Toast =
    Toast.makeText(this, message, Toast.LENGTH_LONG).apply { show() }

fun Context.showLongToast(@StringRes message: Int): Toast =
    Toast.makeText(this, message, Toast.LENGTH_LONG).apply { show() }

fun Context.showShortToast(message: String): Toast =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply { show() }

fun Context.showShortToast(@StringRes message: Int): Toast =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply { show() }
