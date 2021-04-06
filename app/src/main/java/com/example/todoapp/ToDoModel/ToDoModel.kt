package com.example.todoapp.ToDoModel

class ToDoModel {

    companion object Factory {
        fun create(): ToDoModel = ToDoModel()
    }
    var objectId: String? = null
    var itemText: String? = null

}