package com.haroldadmin.kshitijchauhan.resumade.adapter

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.haroldadmin.kshitijchauhan.resumade.R

abstract class SwipeToDeleteCallback(context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

	private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_round_delete_24px)
	private val printIcon = ContextCompat.getDrawable(context, R.drawable.ic_round_print_24px)
	private val background = ColorDrawable()
	private val backgroundColorDelete = ContextCompat.getColor(context, R.color.errorColor)
	private val backgroundColorPrint = ContextCompat.getColor(context, R.color.primaryDarkColor)
	private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

	override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
		return false
	}

	override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

		val itemView = viewHolder.itemView
		val itemHeight = itemView.bottom - itemView.top
		val isCanceled = dX == 0f && !isCurrentlyActive

		if (isCanceled) {
			clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
			super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
			return
		}


		if (dX < 0) {
			val intrinsicWidth = deleteIcon?.intrinsicWidth ?: 0
			val intrinsicHeight = deleteIcon?.intrinsicHeight ?: 0

			// Draw the red delete background
			background.color = backgroundColorDelete
			background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
			background.draw(c)

			// Calculate position of delete icon
			val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
			val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
			val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
			val deleteIconRight = itemView.right - deleteIconMargin
			val deleteIconBottom = deleteIconTop + intrinsicHeight

			// Draw the delete icon
			deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
			deleteIcon?.draw(c)
		} else {
			val intrinsicWidth = printIcon?.intrinsicWidth ?: 0
			val intrinsicHeight = printIcon?.intrinsicHeight ?: 0

			background.color = backgroundColorPrint
			background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
			background.draw(c)

			val printIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
			val printIconMargin = (itemHeight - intrinsicHeight) / 2
			val printIconLeft = itemView.left + printIconMargin
			val printIconRight = itemView.left + printIconMargin + intrinsicWidth
			val printIconBottom = printIconTop + intrinsicHeight

			printIcon?.setBounds(printIconLeft, printIconTop, printIconRight, printIconBottom)
			printIcon?.draw(c)
		}
		super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
	}

	private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
		c?.drawRect(left, top, right, bottom, clearPaint)
	}
}