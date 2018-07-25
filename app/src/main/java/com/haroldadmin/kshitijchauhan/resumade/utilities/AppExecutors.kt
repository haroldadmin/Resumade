package com.haroldadmin.kshitijchauhan.resumade.utilities

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object AppExecutors {

	/*
	A single threaded executor for handling database operations
	Using just one thread to enable sequential read/writes to the
	database in order to avoid race conditions
	 */
	val diskIO : ExecutorService = Executors.newSingleThreadExecutor()

	/*
	A main thread executor to update the UI from some place other
	than an activity or a fragment. Just in case.
	 */
	val mainThreadExecutor : Executor = object : Executor {
		private val mainThreadHandler : Handler = Handler(Looper.getMainLooper())
		override fun execute(task: Runnable) {
			mainThreadHandler.post(task)
		}

	}

}

