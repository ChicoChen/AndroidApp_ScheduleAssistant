package com.example.myapplication

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ui.theme.task_recyclerViewAdapter
import com.google.gson.Gson
import java.util.prefs.Preferences
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

class Fragment1 : Fragment(), ScheduleDialogFragment.OnInputSelect{
    private var taskModels = ArrayList<taskModel>()

    val LOG_TAG = "Fragment_1_LOG"

    private lateinit var adapter: task_recyclerViewAdapter
    private  lateinit var emptyIndicator:TextView
    private lateinit var mLayoutManager:LinearLayoutManager
    private lateinit var dataStore: DataStore<Preferences>
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(LOG_TAG, "onCreateView")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_1, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.scheduleView)
        mLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = mLayoutManager
        adapter = task_recyclerViewAdapter(view.context, taskModels)
        recyclerView.adapter = adapter

        //empty indicating text
        emptyIndicator  =  view.findViewById<TextView>(R.id.ScheduleEmpty)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        inflater.inflate(R.menu.schedule_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addSchedule->{
                val dialog = ScheduleDialogFragment()
                dialog.setTargetFragment(this, 1)
                dialog.show(parentFragmentManager, "myDialogFragment")
                true
            }
            R.id.tagAll->{
                for(task in taskModels){
                    task.done = true
                }
                adapter.notifyDataSetChanged()
                true
            }
            R.id.deleteTag->{
                taskModels.removeIf{it.done}
                adapter.notifyDataSetChanged()
                if (taskModels.size == 0){ emptyIndicator.visibility = View.VISIBLE;}
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    override fun setInput(task: String, hour:String, minute:String){
        taskModels.add(taskModel(task, hour, minute, false))
        taskModels.sortBy { it.mPriority }
        adapter.notifyDataSetChanged()
        if (taskModels.size != 0){ emptyIndicator.visibility = View.INVISIBLE;}
    }

    override fun onPause() {
        super.onPause()
        prefs= activity?.let { PreferenceManager.getDefaultSharedPreferences(it) }!!
        val gson = Gson()
        val json:String = gson.toJson(taskModels)

        val editor = prefs.edit()
        editor.putString("SCHEDULES", json)
        editor.apply()

        Log.i(LOG_TAG, "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.i(LOG_TAG, "onResume")

        prefs= activity?.let { PreferenceManager.getDefaultSharedPreferences(it) }!!

        val gson = Gson()
        val json = prefs.getString("SCHEDULES", null)
        if (json == null){ Log.i(LOG_TAG, "getString failed") }
        else{
            Log.i(LOG_TAG, "fetching data")

            val type:Type = object :TypeToken<ArrayList<taskModel>>(){}.type
            val temp = gson.fromJson<ArrayList<taskModel>>(json, type)
            taskModels.clear()
            taskModels.addAll(temp)
        }

        if (taskModels.size == 0){ emptyIndicator.visibility = View.VISIBLE;}
        adapter.notifyDataSetChanged()
    }
}