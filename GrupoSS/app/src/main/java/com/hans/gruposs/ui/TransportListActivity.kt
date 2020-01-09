package com.hans.gruposs.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.emedinaa.kotlinapp.ui.adapter.TransportAdapter
import com.hans.gruposs.R
import com.hans.gruposs.common.PreferencesHelper
import com.hans.gruposs.model.Transport
import com.hans.gruposs.model.TransportEntity
import com.hans.gruposs.storage.TransportApiClient
import com.hans.gruposs.storage.TransportRepository
import kotlinx.android.synthetic.main.activity_transport_list.*
import kotlinx.android.synthetic.main.layout_loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransportListActivity : AppCompatActivity() {
    private lateinit var transportRepository: TransportRepository

    private var transports: List<TransportEntity>? = null

    private var call: Call<Transport>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setupRepository()
        ui()
    }

    private fun setupRepository() {
        transportRepository = TransportRepository()
    }


    private fun ui() {

        lstNotes.setOnItemClickListener { parent, view, position, id ->
            transports?.let {
                if (it.isNotEmpty()) {
                    val entity: TransportEntity = it[position]
                    goToDetail(entity)
                }
            }
        }
    }

    private fun goToDetail(transport: TransportEntity) {
        val bundle = Bundle()
        bundle.putSerializable("TRANSPORTE", transport)
        val intent = Intent(this, EditTransportActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        loadUser()
        loadTransport()
    }

    private fun loadUser() {
        tviUser.text = "Bienvenido: ".plus(PreferencesHelper.userName(this))
    }

    private fun loadTransport() {
        val userId = PreferencesHelper.userSession(this)
        showLoading()
        call = TransportApiClient.build()?.transports(userId)

        call?.enqueue(object : Callback<Transport> {
            override fun onFailure(call: Call<Transport>, t: Throwable) {
                hideLoading()
                showErrorMessage(t.message)
            }

            override fun onResponse(call: Call<Transport>, response: Response<Transport>) {
                hideLoading()
                response?.body()?.let {
                    if (response.isSuccessful) { //200
                        renderTransport(it.tablaCg)
                    } else {
                        showErrorMessage(response.errorBody()?.string())
                    }
                }
            }
        })
    }

    private fun renderTransport(nTrans: List<TransportEntity>?) {
        transports = nTrans
        transports?.let {
            lstNotes.adapter = TransportAdapter(this, it)
        }
    }


    private fun showErrorMessage(error: String?) {
        Toast.makeText(this, "Error : $error", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        flayLoading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        flayLoading.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun logout(view: View) {
        Log.d("CONSOLE", view.toString())
        PreferencesHelper.clearSession(this)
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
    }
}
