package com.ifmvo.togetherad.core.utils

import android.os.Handler
import android.os.Looper

import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Async {
    internal val cache: Executor = Executors.newCachedThreadPool()
    private val HANDLER = Handler(Looper.getMainLooper())
    internal val main: Executor = Executor { command -> HANDLER.post(command) }
}