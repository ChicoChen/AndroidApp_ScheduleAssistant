package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import java.lang.ClassCastException

class ScheduleDialogFragment: DialogFragment() {
    private lateinit var inputTask:EditText
    private lateinit var inputHour:NumberPicker
    private lateinit var inputMinute:NumberPicker

    interface OnInputSelect{
        fun setInput(task:String, hour:String, minute:String)
    }
    lateinit var myInput:OnInputSelect

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.schedule_dialog, container, false)
        val addButton = view.findViewById<Button>(R.id.addScheduleConfirm)
        inputTask = view.findViewById<EditText>(R.id.newTaskName)

        inputHour = view.findViewById<NumberPicker>(R.id.newTaskHour)
        inputHour.minValue = 0
        inputHour.maxValue = 23

        inputMinute = view.findViewById<NumberPicker>(R.id.newTaskMinute)
        inputMinute.minValue = 0
        inputMinute.maxValue = 5

        val minutes = resources.getStringArray(R.array.minutes)
        inputMinute.displayedValues = minutes

        addButton.setOnClickListener {
            val minutesArray = resources.getStringArray(R.array.minutes)

            val taskName = inputTask.text.toString()
            val hour = inputHour.value.toString()
            val minute = minutesArray[inputMinute.value]

            if(taskName.isNotEmpty()){
                myInput.setInput(taskName, hour, minute)
                dialog?.dismiss()
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            myInput = getTargetFragment() as OnInputSelect
        }catch(e:ClassCastException){
            val TAG = javaClass.simpleName
            Log.d(TAG, "onAttach: ClassCastException" + e.message)
        }
    }
}