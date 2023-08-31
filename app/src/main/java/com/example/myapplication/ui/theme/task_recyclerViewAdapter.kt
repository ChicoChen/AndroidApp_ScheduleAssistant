package com.example.myapplication.ui.theme

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.taskModel

class task_recyclerViewAdapter(context:Context, taskModels:ArrayList<taskModel>) : RecyclerView.Adapter<task_recyclerViewAdapter.myViewHolder>() {
    var context:Context
    var taskModels:ArrayList<taskModel>
    init{
        this.context = context
        this.taskModels = taskModels
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): task_recyclerViewAdapter.myViewHolder {
        // where we inflate the layout
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.schedule_row, parent, false)
        return task_recyclerViewAdapter.myViewHolder(view)
    }

    override fun onBindViewHolder(holder: task_recyclerViewAdapter.myViewHolder, position: Int) {
        // assign value to each row
        holder.task.text = taskModels[position].taskName
        holder.endtime.text = taskModels[position].hour + ":" + taskModels[position].minute
        if(taskModels[position].done){holder.checkbox.setImageResource(R.drawable.check_box_24)}
        else{holder.checkbox.setImageResource(R.drawable.check_box_blank_24)}

        holder.checkbox.setOnClickListener{
            taskModels[position].done = !taskModels[position].done
            this.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return taskModels.size
    }

    class myViewHolder : RecyclerView.ViewHolder{
        // grab view and assign to variable
        var task: TextView
        var endtime:TextView
        var checkbox:ImageView

        constructor(itemView: View):super(itemView){
            task = itemView.findViewById(R.id.taskText)
            endtime = itemView.findViewById(R.id.endTimeText)
            checkbox = itemView.findViewById(R.id.checkbox)
        }
    }

}