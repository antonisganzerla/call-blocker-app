package com.sgztech.callblocker.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.sgztech.callblocker.R

object AlertDialogUtil {

    /**
     * builder with positive and negative callback
     */
    @JvmStatic
    fun create(
        context: Context,
        resourceMessage: Int,
        positiveCallBack : () -> Unit,
        negativeCallBack : () -> Unit
        ): AlertDialog{
        return build(context, resourceMessage, positiveCallBack)
            .setNegativeButton(context.getString(R.string.dialog_negative_button)) { _, _ ->
                negativeCallBack()
            }
            .create()
    }

    /**
     * builder with only positive callback
     */
    @JvmStatic
    fun create(
        context: Context,
        resourceMessage: Int,
        positiveCallBack : () -> Unit
    ): AlertDialog{
        return build(context, resourceMessage, positiveCallBack)
            .setNegativeButton(context.getString(R.string.dialog_negative_button)) { _, _ ->
                // unnecessary negative callback
            }
            .create()
    }

    private fun build(
        context: Context,
        resourceMessage: Int,
        positiveCallBack : () -> Unit
    ): AlertDialog.Builder{
        return AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.dialog_title))
            .setMessage(context.getString(resourceMessage))
            .setPositiveButton(context.getString(R.string.dialog_positive_button)) { _, _ ->
                positiveCallBack()
            }
    }

    @JvmStatic
    fun showSimpleDialog(context: Context, resourceTitle: Int, resourceMessage: Int){
        AlertDialog.Builder(context)
            .setTitle(context.getString(resourceTitle))
            .setMessage(context.getString(resourceMessage))
            .show()
    }
}