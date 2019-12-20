package com.hans.gruposs.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.hans.gruposs.R
import com.hans.gruposs.common.PreferencesHelper
import com.hans.gruposs.storage.LogInResponse
import com.hans.gruposs.storage.TransportApiClient
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.layout_loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInActivity : AppCompatActivity() {

    private var username: String? = null
    private var password: String? = null
    private var call: Call<List<LogInResponse>>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        btnLogin.setOnClickListener {
            if(validateForm()){
                logIn()
            }
        }
    }

    private fun logIn(){
        showLoading()
        call= TransportApiClient.build()?.logIn(username, password)
        call?.enqueue(object :Callback<List<LogInResponse>>{
            override fun onFailure(call: Call<List<LogInResponse>>, t: Throwable) {
                hideLoading()
                showMessage(t.message)
            }

            override fun onResponse(call: Call<List<LogInResponse>>, response: Response<List<LogInResponse>>) {
                hideLoading()
                response?.body()?.let {
                    if(response.isSuccessful){
                        saveSession(it.get(0))
                        goToNoteList()
                    }else{
                        showMessage(response.errorBody()?.string())
                    }
                }
            }
        })
    }

    private fun saveSession(logInResponse: LogInResponse?){
        logInResponse?.let {
            if(it.usuario!=null && it.idusuario!=null){
                PreferencesHelper.saveSession(this,it)
            }
        }
    }

    private fun goToNoteList(){
        val intent= Intent(this,TransportListActivity::class.java)
        startActivity(intent)
    }
    private fun showMessage(message: String?) {
        Toast.makeText(this@LogInActivity,
            "LogIn $message", Toast.LENGTH_LONG).show()
    }

    private fun validateForm(): Boolean {
        username = eteUsername.text.toString()
        password = etePassword.text.toString()

        if (username.isNullOrEmpty()) {
            eteUsername.error="Error campo usuario"
            return false
        }
        if (password.isNullOrEmpty()) {
            etePassword.error="Error campo password"
            return false
        }
        return true
    }

    private fun showLoading() {
        flayLoading.visibility= View.VISIBLE
    }

    private fun hideLoading() {
        flayLoading.visibility= View.GONE
    }
}
