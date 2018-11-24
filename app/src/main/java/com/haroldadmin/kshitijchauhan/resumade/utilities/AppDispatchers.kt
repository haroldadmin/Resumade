package com.haroldadmin.kshitijchauhan.resumade.utilities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object AppDispatchers {

	/*
	A single threaded executor for handling database operations
	Using just one thread to enable sequential read/writes to the
	database in order to avoid race conditions
	 */
	private val diskIO : ExecutorService = Executors.newSingleThreadExecutor()

	val diskDispatcher = diskIO.asCoroutineDispatcher()
	val computationDispatcher = Dispatchers.Default
}

