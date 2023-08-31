package com.example.myapplication.ui.theme

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.taskModel
import com.example.myapplication.todoModel

class todo_recyclerViewAdapter(context: Context, todoModels:ArrayList<todoModel>, listener: RecyclerViewEvent) : RecyclerView.Adapter<todo_recyclerViewAdapter.myViewHolder>(){
    var context:Context
    var todoModels:ArrayList<todoModel>
    var listener: RecyclerViewEvent
    init{
        this.context = context
        this.todoModels = todoModels
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): todo_recyclerViewAdapter.myViewHolder {
        // where we inflate the layout
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.todo_row, parent, false)

        return myViewHolder(view)
    }

    override fun onBindViewHolder(holder: todo_recyclerViewAdapter.myViewHolder, position: Int) {
        // assign value to each row
        holder.todo_title.text = todoModels[position].title
        holder.todo_detail.text = todoModels[position].detail
        holder.todo_detail.isVisible = !todoModels[position].hidden
    }

    override fun getItemCount(): Int {
        return todoModels.size
    }

    inner class myViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        // grab view and assign to variable
        var todo_title: TextView
        var todo_detail: TextView

        constructor(itemView: View):super(itemView){
            todo_title = itemView.findViewById(R.id.todo_title)
            todo_detail = itemView.findViewById(R.id.todo_detail)

            itemView.setOnClickListener(this)
            todo_title.setOnClickListener(this)
            todo_detail.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface RecyclerViewEvent {
        fun onItemClick(position:Int)
    }

}