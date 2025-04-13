package com.example.app_0.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.app_0.data.AppDatabase
import com.example.app_0.data.NotesRepository
import com.example.app_0.databinding.ActivityMainBinding

import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.collectLatest
import androidx.lifecycle.lifecycleScope

import androidx.recyclerview.widget.DefaultItemAnimator


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация адаптера с анимациями
        adapter = NotesAdapter { note ->
            viewModel.deleteNote(note)
        }.apply {
            setHasStableIds(true)  // Включает стабильные ID для анимаций
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = DefaultItemAnimator() // Добавляет стандартные анимации

        // Инициализация базы данных
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "notes-database"
        ).build()

        val repository = NotesRepository(db.noteDao())
        viewModel = NotesViewModel(repository)

        adapter = NotesAdapter(
            onDeleteClick = { note -> viewModel.deleteNote(note) }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Подписываемся на изменения в базе данных
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
                viewModel.addNote(title, content)
                binding.titleEditText.text.clear()
                binding.contentEditText.text.clear()
            }
        }

        binding.addButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            if (title.isNotEmpty() && content.isNotEmpty()) {
                viewModel.addNote(title, content)
                binding.titleEditText.text.clear()
                binding.contentEditText.text.clear()

                // Прокручиваем к новой заметке
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