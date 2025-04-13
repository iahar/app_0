package com.example.app_0.data

class NotesRepository {
    private val notes = mutableListOf<Note>()

    fun getAllNotes(): List<Note> = notes.toList()

    fun addNote(note: Note) {
        notes.add(note)
    }

    fun deleteNote(note: Note) {
        notes.remove(note)
    }
}