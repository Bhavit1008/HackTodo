package com.example.schedulertodo.ui.charts

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.schedulertodo.R
import com.example.schedulertodo.client.RetrofitClient
import com.example.schedulertodo.models.StatusCountResponse
import com.example.schedulertodo.models.TaskCountResponse
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.activity_category_chart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatusChart : AppCompatActivity() {

    private var email: String? = ""
    var Ongoing :Int = 0
    var Completed :Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_chart)
        val actionBar = supportActionBar
        actionBar!!.title = "Status Insights"

        val preference=getSharedPreferences("todo", Context.MODE_PRIVATE)
        val editor=preference.edit()
        email = preference.getString("email","")

        if(email !=null){
            RetrofitClient.instance.taskStatusCount(
                email!!
            ).enqueue(object : Callback<StatusCountResponse> {
                override fun onFailure(call: Call<StatusCountResponse>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error", Toast.LENGTH_SHORT).show()
                    Log.d("error",t.toString())
                }

                override fun onResponse(call: Call<StatusCountResponse>, response: Response<StatusCountResponse>) {
                    Log.d("response",response.body().toString())
                    Ongoing = response.body()?.Ongoing!!
                    Completed = response.body()?.Completed!!
                    setBarChart()
                    Toast.makeText(applicationContext,Ongoing.toString(), Toast.LENGTH_SHORT).show()
                }

            })
        }
        else{
            Toast.makeText(this,"NOT ENTERED", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setBarChart() {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(Ongoing.toFloat(), 0))
        entries.add(BarEntry(Completed.toFloat(), 1))



        val barDataSet = BarDataSet(entries, "Cells")


        val labels = ArrayList<String>()
        labels.add("OnGoing")
        labels.add("Completed")
        val data = BarData(labels, barDataSet)
        barChart.data = data // set the data and list of lables into chart
        barChart.setDescription("Status of Tasks")  // set the description
        barDataSet.color = resources.getColor(R.color.colorAccent)
        barChart.animateY(3000)
    }

}
