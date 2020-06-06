package com.example.schedulertodo.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.view.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.schedulertodo.R
import com.example.schedulertodo.auth.LoginActivity
import com.example.schedulertodo.db.NoteDatabase
import com.example.schedulertodo.ui.charts.CategoryChart
import com.example.schedulertodo.ui.charts.StatusChart
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment() {

    var email:String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        recycler_view_notes.setHasFixedSize(true)
        recycler_view_notes.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)


        launch {
            context?.let{
                val notes = NoteDatabase(it).getNoteDao().getAllNotes()
                recycler_view_notes?.adapter = NotesAdapter(notes)
            }
        }


        button_add.setOnClickListener {
            val action = HomeFragmentDirections.actionAddNotes()
            Navigation.findNavController(it).navigate(action)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cat_insights -> {
                val i = Intent(context,CategoryChart::class.java)
                startActivity(i)
            }

            R.id.menu_logout->{
                val preference=activity?.getSharedPreferences("todo", Context.MODE_PRIVATE)
                val editor=preference?.edit()
                editor?.putString("token","")
                editor?.commit()
                var i = Intent(activity,LoginActivity::class.java)
                startActivity(i)
            }

            R.id.status_insights->{
                val i = Intent(context,StatusChart::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }


}