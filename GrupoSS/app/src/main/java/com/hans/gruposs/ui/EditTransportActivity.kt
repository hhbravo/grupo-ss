package com.hans.gruposs.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.hans.gruposs.R
import com.hans.gruposs.model.Transport
import com.hans.gruposs.model.TransportEntity
import com.hans.gruposs.storage.TransportApiClient
import com.hans.gruposs.storage.TransportRepository
import kotlinx.android.synthetic.main.activity_edit_transport.*
import kotlinx.android.synthetic.main.layout_loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditTransportActivity : AppCompatActivity() {

    private val PERMISSION_ID = 42
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var spinner: Spinner
    private lateinit var transport: TransportEntity
    private lateinit var transportRepository: TransportRepository
    private var call: Call<String>? = null


    private var status: String? = null
    private var observation: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transport)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        verifyExtras()
        setupRepository()
        populate()

        ui()
        getLastLocation()
        this.spinner = spStatus
        //culminado 3
        //rechazado 4
        val statusList = arrayOf("Seleccione", "Culminado", "Rechazado")

        val adapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            statusList
        )

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                status = statusList.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun ui() {
        btnEditTransport.setOnClickListener {
            if (validateForm()) {
                editNote()
            } else {
                showErrorMessage("Ingrese el estado correctamente")
            }
        }

    }


    private fun editNote() {
        showLoading()
        observation = etviObservation.text.toString()
        call = TransportApiClient.build()
            ?.updateTransport(transport.id, getStatusCode(), observation, latitude, longitude)

        call?.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                hideLoading()
                showErrorMessage(t.message)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                hideLoading()
                response?.body()?.let {
                    if (response.isSuccessful) {
                        finish()
                    } else {
                        showErrorMessage(response.errorBody()?.string())
                    }
                }
            }
        })

    }

    private fun getStatusCode() :String {
        when(status) {
            "Culminado" -> return "3"
            else -> return "4"
        }
    }

    private fun validateForm(): Boolean {

        if (status.isNullOrEmpty()) {
            return false
        }
        when (status) {
            "Seleccione" -> return false
            else -> return true
        }
    }

    private fun populate() {
        transport?.let {
            etvId.setText(it?.id)
            etvClient.setText(it?.cliente)
            etvService.setText(it?.tipo_servicio)
            etvOrden.setText(it?.nro_orden)
        }
    }

    private fun setupRepository() {
        transportRepository = TransportRepository()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude.toString()
            longitude = mLastLocation.longitude.toString()
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
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

    private fun verifyExtras() {
        intent?.extras?.let {
            transport = it.getSerializable("TRANSPORTE") as TransportEntity
        }
    }
}