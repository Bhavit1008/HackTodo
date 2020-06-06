package com.example.schedulertodo.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation

import com.example.schedulertodo.R
import com.example.schedulertodo.client.RetrofitClient
import com.example.schedulertodo.db.Note
import com.example.schedulertodo.db.NoteDatabase
import com.example.schedulertodo.models.TaskResponse
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    var stat :String = ""
    var date :String = ""
    var token:String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val preference= activity?.getSharedPreferences("todo", Context.MODE_PRIVATE)
        token= preference?.getString("token","").toString()
        Toast.makeText(activity,token,Toast.LENGTH_SHORT).show()
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
            txt_date.setText(note?.date)
        }

        val languages = resources.getStringArray(R.array.Languages)
        val status = resources.getStringArray(R.array.Status)



        if (spinner_status != null) {
            val adapter1 = ArrayAdapter(context,
                android.R.layout.simple_spinner_item, status)
            spinner_status.adapter = adapter1

            spinner_status.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                    stat = status[position]
                    Toast.makeText(context,
                        getString(R.string.selected_item) + " " +
                                "" + status[position], Toast.LENGTH_SHORT).show()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    Toast.makeText(context,"please select status",Toast.LENGTH_SHORT).show()
                }
            }
        }




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

            if (stat == "") {
                Toast.makeText(context,"please enter the date for task",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            launch {

                context?.let {
                    val mNote = Note(noteTitle, noteBody,date,category,stat)

                    if (note == null) {
                        NoteDatabase(it).getNoteDao().addNote(mNote)
                        Toast.makeText(context,"Task saved",Toast.LENGTH_SHORT).show()
                    } else {
                        mNote.id = note!!.id
                        NoteDatabase(it).getNoteDao().updateNote(mNote)
                        Toast.makeText(context,"Task updated",Toast.LENGTH_SHORT).show()
                    }

                    if(noteTitle !=null && noteTitle !=null && date !=null && stat != null && category !=null){
                        RetrofitClient.instance.addTask(
                            noteTitle,
                            noteBody,
                            date,
                            statusOfProcess(stat),
                            category,
                            token
                        ).enqueue(object : Callback<TaskResponse> {
                            override fun onFailure(call: Call<TaskResponse>, t: Throwable) {
                                Toast.makeText(activity,t.message.toString(),Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(
                                call: Call<TaskResponse>,
                                response: Response<TaskResponse>
                            ) {
                                Toast.makeText(activity,response.body().toString(),Toast.LENGTH_SHORT).show()
                            }


                        })
                    }
                    else{
                        Toast.makeText(activity,"NOT ENTERED",Toast.LENGTH_SHORT).show()
                    }



                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view).navigate(action)
                }
            }

        }

    }

    private fun statusOfProcess(stat: String): String {
        if(stat == "On Going")
            return "0"
        else
            return "1"
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