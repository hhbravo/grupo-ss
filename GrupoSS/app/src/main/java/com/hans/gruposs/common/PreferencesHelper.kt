package com.hans.gruposs.common

import android.content.Context
import android.content.SharedPreferences
import com.hans.gruposs.storage.LogInResponse


object PreferencesHelper {

    private val MYNOTES_PREFERENCES = "com.hans.gruposs"
    private val PREFERENCES_USERNAME = "$MYNOTES_PREFERENCES.username"
    private val PREFERENCES_USERNICK= "$MYNOTES_PREFERENCES.usernick"
    private val PREFERENCES_USERFULLNAME="$MYNOTES_PREFERENCES.userfullname"
    private val PREFERENCES_USERLASTNAME = "$MYNOTES_PREFERENCES.userlastname"


    fun saveSession(context: Context, logInResponse:LogInResponse ) {
        val editor = getEditor(context)
        editor.putString(PREFERENCES_USERNAME, logInResponse.idusuario)
        editor.putString(PREFERENCES_USERFULLNAME, logInResponse.nombres)
        editor.putString(PREFERENCES_USERLASTNAME, logInResponse.apellidos)
        editor.putString(PREFERENCES_USERNICK, logInResponse.usuario)
        editor.apply()
    }

    fun userSession(context: Context): String? {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString(PREFERENCES_USERNAME, null)
    }

    fun userName(context: Context): String? {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString(PREFERENCES_USERFULLNAME, null)
    }

    fun isSignedIn(context: Context): Boolean {
        val preferences = getSharedPreferences(context)
        return preferences.contains(PREFERENCES_USERNAME)
    }

    fun clearSession(context: Context){
        val editor = getEditor(context)
        editor.remove(PREFERENCES_USERNAME)
        editor.remove(PREFERENCES_USERFULLNAME)
        editor.remove(PREFERENCES_USERLASTNAME)
        editor.remove(PREFERENCES_USERNICK)
        editor.apply()
    }

    fun clear(context: Context){
        val editor = getEditor(context)
        editor.clear()
        editor.apply()
    }

    private fun getEditor(context: Context): SharedPreferences.Editor {
        val preferences = getSharedPreferences(context)
        return preferences.edit()
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(MYNOTES_PREFERENCES, Context.MODE_PRIVATE)
    }
}