package com.example.app_0.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.room.Room
import com.example.app_0.R
import com.example.app_0.data.AppDatabase
import com.example.app_0.data.NotesRepository
import com.example.app_0.databinding.ActivityMainBinding
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import com.example.app_0.data.Note

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var adapter: NotesAdapter
    private var currentEditingNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "notes-database"
        ).build()

        val repository = NotesRepository(db.noteDao())
        viewModel = NotesViewModel(repository)

        adapter = NotesAdapter(
            onDeleteClick = { note -> viewModel.deleteNote(note) },
            onEditClick = { note ->
                currentEditingNote = note
                binding.titleEditText.setText(note.title)
                binding.contentEditText.setText(note.content)
                binding.addButton.text = getString(R.string.update_note)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = DefaultItemAnimator()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allNotes.collect { notes ->
                    adapter.submitList(notes)
                }
            }
        }

        binding.addButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                if (currentEditingNote != null) {
                    val updatedNote = currentEditingNote!!.copy(
                        title = title,
                        content = content
                    )
                    viewModel.updateNote(updatedNote)
                    currentEditingNote = null
                    binding.addButton.text = getString(R.string.add_note)
                } else {
                    viewModel.addNote(title, content)
                }

                binding.titleEditText.text.clear()
                binding.contentEditText.text.clear()

                lifecycleScope.launch {
                    viewModel.allNotes.collect { notes ->
                        adapter.submitList(notes)
                        if (notes.isNotEmpty()) {
                            binding.recyclerView.smoothScrollToPosition(0)
                        }
                    }
                }
            }
        }
    }
}