package com.haroldadmin.kshitijchauhan.resumade.adapter

import android.support.v7.util.DiffUtil
import com.haroldadmin.kshitijchauhan.resumade.repository.database.ResumeEntity

class DiffUtilCallback<T : ResumeEntity> (
		val oldList : List<T>,
		val newList : List<T>): DiffUtil.Callback() {

	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return oldList[oldItemPosition].id == newList[newItemPosition].id
	}

	override fun getOldListSize(): Int = oldList.size

	override fun getNewListSize(): Int = newList.size

	override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return oldList[oldItemPosition] == newList[newItemPosition]
	}
}