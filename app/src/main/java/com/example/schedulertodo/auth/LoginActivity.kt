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
import com.example.schedulertodo.ui.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.edt_email
import kotlinx.android.synthetic.main.activity_login.edt_password
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    var pass:String = ""
    var email:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener {
            email = edt_email.text.toString()
            pass = edt_password.text.toString()
            if(email.isEmpty()){
                edt_email.setError("Please enter email")
            }
            if (pass.isEmpty()){
                edt_password.setError("please enetr password")
            }

            if(email !=null && pass !=null){
                RetrofitClient.instance.loginUser(
                    email,
                    pass
                ).enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext,"Error",Toast.LENGTH_SHORT).show()
                        Log.d("error",t.toString())
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        Log.d("response",response.body().toString())
                        val token = response.body().toString().substringAfter("LoginResponse(token=").substringBefore(",")
                        val preference=getSharedPreferences("todo", Context.MODE_PRIVATE)
                        val editor=preference.edit()
                        editor.putString("email",email)
                        editor.putString("token",token)
                        editor.commit()
                        Log.d("token",token)
                        Toast.makeText(applicationContext,token,Toast.LENGTH_SHORT).show()
                        //Toast.makeText(applicationContext,response.body().toString(),Toast.LENGTH_SHORT).show()
                        val i = Intent(applicationContext,MainActivity::class.java)
                        startActivity(i)
                    }

                })
            }
            else{
                Toast.makeText(this,"NOT ENTERED",Toast.LENGTH_SHORT).show()
            }

        }

        goto_reg.setOnClickListener {
            val i =Intent(this,RegistrationActivity::class.java)
            startActivity(i)
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        moveTaskToBack(true);
    }
}
