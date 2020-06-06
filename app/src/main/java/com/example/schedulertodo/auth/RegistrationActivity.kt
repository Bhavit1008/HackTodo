package com.example.schedulertodo.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.schedulertodo.R
import com.example.schedulertodo.client.RetrofitClient
import com.example.schedulertodo.models.LoginResponse
import com.example.schedulertodo.models.RegistrationResponse
import com.example.schedulertodo.ui.MainActivity
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {

    var email: String = ""
    var name: String = ""
    var password: String = ""
    var number: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        btn_register.setOnClickListener {
            email = edt_email.text.toString()
            name = edt_name.text.toString()
            password = edt_password.text.toString()
            number = edt_number.text.toString()
            if (email != null && password != null && name != null && number != null) {
                RetrofitClient.instance.createUser(
                    name,
                    number,
                    email,
                    password
                ).enqueue(object : Callback<RegistrationResponse> {
                    override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<RegistrationResponse>,
                        response: Response<RegistrationResponse>
                    ) {
                        Toast.makeText(
                            applicationContext,
                            response.message().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        val token = response.body().toString().substringAfter("LoginResponse(token=").substringBefore(",")
                        val preference=getSharedPreferences("todo", Context.MODE_PRIVATE)
                        val editor=preference.edit()
                        editor.putString("email",email)
                        editor.putString("token",token)
                        editor.commit()

                        val i = Intent(applicationContext,MainActivity::class.java)
                        startActivity(i)
                    }

                })
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        moveTaskToBack(true);
    }
}
