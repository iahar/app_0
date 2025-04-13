package com.example.app_0.ui

import androidx.lifecycle.ViewModel
import com.example.app_0.data.Note
import com.example.app_0.data.NotesRepository

class NotesViewModel : ViewModel() {
    private val repository = NotesRepository()
    val notes = repository.getAllNotes()

    fun addNote(title: String, content: String) {
        repository.addNote(Note(title = title, content = content))
    }

    fun deleteNote(note: Note) {
        repository.deleteNote(note)
    }
}