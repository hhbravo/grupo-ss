package com.emedinaa.kotlinapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.hans.gruposs.R
import com.hans.gruposs.model.TransportEntity

class TransportAdapter(val context:Context, val transports:List<TransportEntity>):BaseAdapter(){

    private val mInflater: LayoutInflater=LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View?
        val vh: ViewHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.row_item, parent, false)
            vh = ViewHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }
        val client =transports[position].cliente
        val hour =transports[position].hora_entrega
        val order =transports[position].nro_orden
        val id =transports[position].id
        val service =transports[position].tipo_servicio
        vh.tviAccount.text = "Cliente: $client"
        vh.tviHour.text = "Entrega: $hour"
        vh.tviOrder.text = "Num. Orden: $order"
        vh.tviUnique.text = "ID: $id"
        vh.tviService.text = "Servicio: $service"
        return view
    }

    override fun getItem(position: Int): Any {
        return transports[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
       return transports.size
    }

    class ViewHolder(view:View){
        //val iviNote= view.findViewById<ImageView>(R.id.imageViewNote)
        val tviAccount= view.findViewById<TextView>(R.id.tviAccount)
        val tviHour= view.findViewById<TextView>(R.id.tviHour)
        val tviOrder= view.findViewById<TextView>(R.id.tviOrder)
        val tviUnique= view.findViewById<TextView>(R.id.tviUnique)
        val tviService= view.findViewById<TextView>(R.id.tviService)
    }
}