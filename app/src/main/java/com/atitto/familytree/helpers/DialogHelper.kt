package com.atitto.familytree.helpers

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import com.atitto.domain.auth.Sex
import com.atitto.familytree.R
import java.util.*

object DialogHelper {

    fun calendarDialogPast(context: Context,
                           calendar: Calendar = Calendar.getInstance(),
                           listener: DatePickerDialog.OnDateSetListener) {
        val dialog = DatePickerDialog(
            context,
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.datePicker.maxDate = System.currentTimeMillis()
        dialog.show()
    }

    fun sexDialogChoose(context: Context, currentlyChosen: Int, listener: (Sex) -> Unit) {
        AlertDialog.Builder(context)
            .setTitle(R.string.sex)
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .setSingleChoiceItems(Sex.getTitles(context), currentlyChosen) { dialog, item ->
                listener.invoke(Sex.values()[item])
                dialog.dismiss()
            }.show()

    }

}