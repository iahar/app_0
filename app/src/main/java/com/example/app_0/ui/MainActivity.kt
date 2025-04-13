package com.example.app_0.ui

import com.example.app_0.data.Note

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_0.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = NotesViewModel()
        adapter = NotesAdapter(
            notes = viewModel.notes,
            onDeleteClick = { note: Note -> viewModel.deleteNote(note) }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.addButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            if (title.isNotEmpty() && content.isNotEmpty()) {
                viewModel.addNote(title, content)
                adapter.notifyDataSetChanged()
                binding.titleEditText.text.clear()
                binding.contentEditText.text.clear()
            }
        }
    }
}