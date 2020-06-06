package com.example.schedulertodo.ui.charts

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.schedulertodo.R
import com.example.schedulertodo.client.RetrofitClient
import com.example.schedulertodo.models.LoginResponse
import com.example.schedulertodo.models.TaskCountResponse
import com.example.schedulertodo.ui.MainActivity
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.activity_category_chart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryChart : AppCompatActivity() {

    private var Shopping:Int  = 0
    private var Personal: Int = 0
    private var Work: Int = 0
    
    private var Other: Int = 0

    private val model: PersonalViewModel by viewModels()

    var email : String = ""
    var name :String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_chart)
        val actionBar = supportActionBar
        actionBar!!.title = "Category Insights"

        val preference=getSharedPreferences("todo", Context.MODE_PRIVATE)
        val editor=preference.edit()
        email = preference.getString("email","")

        if(email !=null){
            RetrofitClient.instance.taskCount(
                email
            ).enqueue(object : Callback<TaskCountResponse> {
                override fun onFailure(call: Call<TaskCountResponse>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error", Toast.LENGTH_SHORT).show()
                    Log.d("error",t.toString())
                }

                override fun onResponse(call: Call<TaskCountResponse>, response: Response<TaskCountResponse>) {
                    Log.d("response",response.body().toString())
                     Personal = response.body()?.Personal!!
                     Work = response.body()?.Work!!
                     Shopping = response.body()?.Shopping!!
                     Other = response.body()?.Other!!
                    setBarChart()
                    Toast.makeText(applicationContext,Personal.toString(),Toast.LENGTH_SHORT).show()
                }

            })
        }
        else{
            Toast.makeText(this,"NOT ENTERED", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setBarChart() {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(Personal.toFloat(), 0))
        entries.add(BarEntry(Work.toFloat(), 1))
        entries.add(BarEntry(Shopping.toFloat(), 2))
        entries.add(BarEntry(Other.toFloat(), 3))



        val barDataSet = BarDataSet(entries, "Cells")


        val labels = ArrayList<String>()
        labels.add("Personal")
        labels.add("Work")
        labels.add("Shopping")
        labels.add("Other")
        val data = BarData(labels, barDataSet)
        barChart.data = data // set the data and list of lables into chart
        barChart.setDescription("Categorial Analysis of Set Task")  // set the description
        barDataSet.color = resources.getColor(R.color.colorAccent)
        barChart.animateY(3000)
    }


}
