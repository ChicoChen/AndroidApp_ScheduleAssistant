package com.example.myapplication

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myapplication.ui.theme.mItemTouchInterface
import com.example.myapplication.ui.theme.todo_recyclerViewAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.Collections

class Fragment2 : Fragment(), todo_recyclerViewAdapter.RecyclerViewEvent, mTodoInterface, mItemTouchInterface{
    private var todoModels = ArrayList<todoModel>()
    val LOG_TAG = "Fragment_2_LOG"

    private lateinit var adapter: todo_recyclerViewAdapter
    private lateinit var mLayoutManager:LinearLayoutManager
    private  lateinit var emptyIndicator: TextView
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
        val view = inflater.inflate(R.layout.fragment_2, container, false)

        // setup recyclerview
        val recyclerView = view.findViewById<RecyclerView>(R.id.todoView)
        mLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = mLayoutManager
        adapter = todo_recyclerViewAdapter(view.context, todoModels, this)
        recyclerView.adapter = adapter

        //empty indicating text
        emptyIndicator = view.findViewById<TextView>(R.id.todoEmpty)

        // ItemTouchHelper setting (for item drag and sweep)
        val mCallback = mItemTouchCallback(this)
        val helper = ItemTouchHelper(mCallback)
        helper.attachToRecyclerView(recyclerView)

        return view
    }

    override fun onPause() {
        super.onPause()

        todoModels.forEach { it.hidden = true }

        prefs= activity?.let { PreferenceManager.getDefaultSharedPreferences(it) }!!
        val gson = Gson()
        val json:String = gson.toJson(todoModels)

        val editor = prefs.edit()
        editor.putString("TODOS", json)
        editor.apply()

        Log.i(LOG_TAG, "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.i(LOG_TAG, "onResume")

        prefs= activity?.let { PreferenceManager.getDefaultSharedPreferences(it) }!!

        val gson = Gson()
        val json = prefs.getString("TODOS", null)
        if (json == null){ Log.i(LOG_TAG, "getString failed") }
        else{
            Log.i(LOG_TAG, "fetching data")

            val type: Type = object : TypeToken<ArrayList<todoModel>>(){}.type
            val temp = gson.fromJson<ArrayList<todoModel>>(json, type)
            todoModels.clear()
            todoModels.addAll(temp)
        }

        if (todoModels.size == 0){ emptyIndicator.visibility = View.VISIBLE;}
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        inflater.inflate(R.menu.todo_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addTodo->{
                val add_dialog = AddTodoDialog()
                add_dialog.setTargetFragment(this, 1)
                add_dialog.show(parentFragmentManager, "clearTodoDialog")
                true
            }
            R.id.clearAllTodo->{
                val clear_dialog = ClearTodoDialog()
                clear_dialog.setTargetFragment(this, 1)
                clear_dialog.show(parentFragmentManager, "clearTodoDialog")
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(position: Int) {
        todoModels[position].hidden = !todoModels[position].hidden
        Log.i(LOG_TAG, "onclick")
        adapter.notifyDataSetChanged()
    }

    override fun addNewTodo(title: String, detail: String) {
        if(title.isNotEmpty() && detail.isNotEmpty()){
            todoModels.add(todoModel(title, detail))
            emptyIndicator.visibility = View.INVISIBLE;

            adapter.notifyDataSetChanged()
        }
    }

    override fun clearItems() {
        todoModels.clear()
        emptyIndicator.visibility = View.VISIBLE;
        adapter.notifyDataSetChanged()
    }

    override fun replaceItem(from: Int, to: Int) {
        Log.i(LOG_TAG, "moveItem $from to $to")
        Collections.swap(todoModels, from, to)
        adapter.notifyItemMoved(from, to)
    }

    override fun deleteItem(position:Int){
        Log.i(LOG_TAG, "deleteItem $position")
        val temp = todoModels[position]
        todoModels.removeAt(position)
        if(todoModels.isEmpty()){ emptyIndicator.visibility = View.VISIBLE };

        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, adapter.itemCount - position)

        val conLayout = view?.findViewById<ConstraintLayout>(R.id.fragment2_layout)
        val snackbar = conLayout?.let { Snackbar.make(it, "Item was removed.", Snackbar.LENGTH_LONG) }
        if (snackbar != null) {
            val bottomNavView = requireActivity().findViewById<View>(R.id.navigation)
            snackbar.anchorView = bottomNavView
            snackbar.setAction("UNDO", View.OnClickListener() {
                todoModels.add(position, temp)
                adapter.notifyItemInserted(position)
            })
            snackbar.show();
        }
    }
}