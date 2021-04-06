package com.example.todoapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.ToDoModel.ToDoModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*


class MainActivity : AppCompatActivity(), Delete {

    var toDoItemList: MutableList<ToDoModel>? = null
    lateinit var adapter: Adapter
    lateinit var mDatabase: DatabaseReference
    private var listViewItem: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            addNewItemDialog()
        }

        mDatabase = FirebaseDatabase.getInstance().reference
        toDoItemList = mutableListOf<ToDoModel>()
        adapter = Adapter(this, toDoItemList!!)
        listViewItem = findViewById(R.id.List_view)
        listViewItem!!.adapter = adapter

        mDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                toDoItemList!!.clear()
                addDataToList(dataSnapshot)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, "Item Adding Failed!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addDataToList(dataSnapshot: DataSnapshot) {

        val items = dataSnapshot.children.iterator()

        if (items.hasNext()) {
            val toDoListindex = items.next()
            val itemsIterator = toDoListindex.children.iterator()

            while (itemsIterator.hasNext()) {
                val currentItem = itemsIterator.next()
                val todoItem = ToDoModel.create()
                val map = currentItem.value as HashMap<String, Any>

                todoItem.objectId = currentItem.key
                todoItem.itemText = map["itemText"] as String?
                toDoItemList!!.add(todoItem)
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun addNewItemDialog() {
        val alert = AlertDialog.Builder(this)
        val itemEditText = EditText(this)
        alert.setMessage("Add New Task")
        alert.setTitle("Enter To Do Task Text")
        alert.setView(itemEditText)
        alert.setPositiveButton("Add") { dialog, positiveButton ->
            val todoItem = ToDoModel.create()
            todoItem.itemText = itemEditText.text.toString()
            val newItem = mDatabase.child("ToDos").push()
            todoItem.objectId = newItem.key
            newItem.setValue(todoItem)
            dialog.dismiss()
            Toast.makeText(this, "Item saved!", Toast.LENGTH_SHORT).show()
        }
        alert.show()
    }

    override fun onItemDelete(itemObjectId: String) {
        val itemReference = mDatabase.child("ToDos").child(itemObjectId)
        itemReference.removeValue()
        adapter.notifyDataSetChanged()
    }
}