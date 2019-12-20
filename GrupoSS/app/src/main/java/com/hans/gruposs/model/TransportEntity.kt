package com.hans.gruposs.model

import java.io.Serializable

data class Transport(val tablaCg: List<TransportEntity>)

data class TransportEntity(
    val id: String?,
    val fecha_creacion: String?,
    val nro_orden: String?,
    val tipo_servicio: String?,
    val sectorista: String?,
    val cliente: String?,
    val punto_retiro: String?,
    val punto_llegada: String?,
    val hora_entrega: String?,
    val contacto: String?,
    val numero_contacto: String?,
    val transporte: String?,
    val tipo_fcl: String?,
    val servicio_adicional: String?,
    val contenedor: String?,
    val volumen: String?,
    val peso: String?,
    val observacion: String?,
    val hora_solicitud: String?,
    val hora_termino: String?,
    val almacen_vacio: String?,
    val idasignado: String?,
    val idasigna: String?,
    val idcrea: String?,
    val estado: String?,
    val crea: String?,
    val asigna: String?,
    val asignado: String?

) : Serializable