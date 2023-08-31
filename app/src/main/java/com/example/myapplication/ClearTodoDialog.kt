package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class ClearTodoDialog: DialogFragment() {
    lateinit var mTodo:mTodoInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.cleartodo_dialog, container, false)

        val  clearButton = view.findViewById<Button>(R.id.clearButton)
        clearButton.setOnClickListener {
            mTodo.clearItems()
            dialog?.dismiss()
        }

        val  backButton = view.findViewById<Button>(R.id.leaveClear)
        backButton.setOnClickListener {
            dialog?.dismiss()
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mTodo = getTargetFragment() as mTodoInterface
    }
}