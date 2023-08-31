package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class AddTodoDialog: DialogFragment() {
    lateinit var mTodo: mTodoInterface
    private lateinit var inputTitle: EditText
    private lateinit var inputDetail: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.addtodo_dialog, container, false)
        inputTitle = view.findViewById<EditText>(R.id.editTextTitle)
        inputDetail = view.findViewById<EditText>(R.id.editTextDetail)
        val addButton = view.findViewById<Button>(R.id.addTodoButton)
        addButton.setOnClickListener{
            val title = inputTitle.text.toString()
            var detail = inputDetail.text.toString()
            if(title.isNotEmpty()){
                if(detail.isEmpty()){ detail = "None"}
                mTodo.addNewTodo(title, detail)
                dialog?.dismiss()
            }
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mTodo = getTargetFragment() as mTodoInterface
    }
}