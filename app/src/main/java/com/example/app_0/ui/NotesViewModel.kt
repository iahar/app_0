package com.example.app_0.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_0.data.Note
import com.example.app_0.data.NotesRepository
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {
    val allNotes = repository.allNotes

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            repository.addNote(Note(title = title, content = content))
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}