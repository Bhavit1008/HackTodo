package com.example.schedulertodo.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.schedulertodo.R
import com.example.schedulertodo.db.Note
import com.example.schedulertodo.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class AddNoteFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button_save.setOnClickListener {
            val noteTitle = edit_text_title.text.toString().trim()
            val noteBody = edit_text_note.text.toString().trim()
            if (noteTitle == null){
                edit_text_title.error = "title required"
                edit_text_title.requestFocus()
                return@setOnClickListener
            }
            if (noteBody == null){
                edit_text_note.error = "title required"
                edit_text_note.requestFocus()
                return@setOnClickListener
            }

            launch {
                val note = Note(noteTitle,noteBody)
                context?.let {
                    NoteDatabase(activity!!).getNoteDao().addNote(note)
                    Toast.makeText(it,"Added Task",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
