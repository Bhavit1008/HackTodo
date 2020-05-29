package com.example.schedulertodo.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation

import com.example.schedulertodo.R
import com.example.schedulertodo.db.Note
import com.example.schedulertodo.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch
import java.nio.file.Files.delete
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddNoteFragment : BaseFragment() {

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    private var note: Note? = null
    var category :String = ""
    var date :String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            note = AddNoteFragmentArgs.fromBundle(it).note
            edit_text_title.setText(note?.title)
            edit_text_note.setText(note?.note)
        }

        val languages = resources.getStringArray(R.array.Languages)

        if (spinner != null) {
            val adapter = ArrayAdapter(context,
                android.R.layout.simple_spinner_item, languages)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                    category = languages[position]
                    Toast.makeText(context,
                        getString(R.string.selected_item) + " " +
                                "" + languages[position], Toast.LENGTH_SHORT).show()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    Toast.makeText(context,"please select category",Toast.LENGTH_SHORT).show()
                }
            }
        }

        date_picker.setOnClickListener {
            val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                txt_date.setText("" + dayOfMonth + " " + month + ", " + year)
            }, year, month, day)
            dpd.show()
        }





        button_save.setOnClickListener { view ->

            val noteTitle = edit_text_title.text.toString().trim()
            val noteBody = edit_text_note.text.toString().trim()

            date = txt_date.text.toString()

            val languages = resources.getStringArray(R.array.Languages)

            // access the spinner



            if (noteTitle.isEmpty()) {
                edit_text_title.error = "title required"
                edit_text_title.requestFocus()
                return@setOnClickListener
            }

            if (noteBody.isEmpty()) {
                edit_text_note.error = "note required"
                edit_text_note.requestFocus()
                return@setOnClickListener
            }

            if (category.isEmpty()) {
                Toast.makeText(context,"please select category",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (date == "") {
                Toast.makeText(context,"please enter the date for task",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            launch {

                context?.let {
                    val mNote = Note(noteTitle, noteBody,date,category)

                    if (note == null) {
                        NoteDatabase(it).getNoteDao().addNote(mNote)
                        Toast.makeText(context,"Task saved",Toast.LENGTH_SHORT).show()
                    } else {
                        mNote.id = note!!.id
                        NoteDatabase(it).getNoteDao().updateNote(mNote)
                        Toast.makeText(context,"Task updated",Toast.LENGTH_SHORT).show()
                    }


                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view).navigate(action)
                }
            }

        }

    }

    private fun deleteNote() {
        context?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Are you sure?")
                setMessage("You cannot undo this operation")
                setPositiveButton("Yes") { _, _ ->
                    launch {
                        NoteDatabase(context).getNoteDao().deleteNote(note!!)
                        val action = AddNoteFragmentDirections.actionSaveNote()
                        Navigation.findNavController(view!!).navigate(action)
                    }
                }
                setNegativeButton("No") { _, _ ->

                }
            }.create().show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> if (note != null) deleteNote() else Toast.makeText(context,"deleted",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }
}