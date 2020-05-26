package com.example.clinicenrolment.common

import android.content.Context
import android.content.SharedPreferences

public open class Session {

    private var context: Context? = null
    private var sessionName: String? = null
    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null


    protected fun createSession(context: Context, sessionName: String): Session {
        val s = Session()
        s.context = context
        s.sessionName = sessionName
        s.prefs = context.getSharedPreferences(sessionName, Context.MODE_PRIVATE)
        return s
    }

    fun putString(key: String, value: String) {
        editor = prefs!!.edit()
        editor!!.putString(key, value)
        editor!!.commit()
    }

    fun putBoolean(key: String, value: Boolean) {
        editor = prefs!!.edit()
        editor!!.putBoolean(key, value)
        editor!!.commit()
    }

    protected fun putInt(key: String, value: Int) {
        editor = prefs!!.edit()
        editor!!.putInt(key, value)
        editor!!.commit()
    }

    protected fun putFloat(key: String, value: Float) {
        editor = prefs!!.edit()
        editor!!.putFloat(key, value)
        editor!!.commit()
    }

    protected fun putLong(key: String, value: Long) {
        editor = prefs!!.edit()
        editor!!.putLong(key, value)
        editor!!.commit()
    }

    protected fun putStringSet(key: String, value: Set<String>) {
        editor = prefs!!.edit()
        editor!!.putStringSet(key, value)
        editor!!.commit()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs!!.getBoolean(key, defaultValue)
    }

     fun getString(key: String, defaultValue: String): String {
        return prefs!!.getString(key, defaultValue)!!
    }

    protected fun getFloat(key: String, defaultValue: Float): Float {
        return prefs!!.getFloat(key, defaultValue)
    }

    protected fun getInt(key: String, defaultValue: Int): Int {
        return prefs!!.getInt(key, defaultValue)
    }

    protected fun getLong(key: String, defValue: Long): Long {
        return prefs!!.getLong(key, defValue)
    }

    protected fun getStringSet(key: String, defValue: Set<String>): Set<String> {
        return prefs!!.getStringSet(key, defValue)!!
    }

    protected fun remove(key: String) {
        editor = prefs!!.edit()
        editor!!.remove(key)
        editor!!.commit()
    }
}
