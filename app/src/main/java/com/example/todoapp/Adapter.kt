package com.example.todoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.todoapp.R.layout.todo_item
import com.example.todoapp.ToDoModel.ToDoModel

class Adapter (context: Context, toDoItemList: MutableList<ToDoModel>) : BaseAdapter() {

    private var rowListener: Delete = context as Delete
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var itemList = toDoItemList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val objectId: String = itemList[position].objectId as String
        val itemText: String = itemList[position].itemText as String

        val view: View
        val vh: ListRowHolder

        if (convertView == null) {
            view = mInflater.inflate(todo_item, parent, false) as View
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        vh.label.text = itemText

        vh.delete.setOnClickListener {
            rowListener.onItemDelete(objectId)
        }
        return view
    }
    override fun getItem(index: Int): Any {
        return itemList[index]
    }
    override fun getItemId(index: Int): Long {
        return index.toLong()
    }
    override fun getCount(): Int {
        return itemList.size
    }
    private class ListRowHolder(row: View) {
        val label: TextView = row!!.findViewById(R.id.text_view)
        val delete: ImageButton = row!!.findViewById(R.id.dlt_btn)
    }
}