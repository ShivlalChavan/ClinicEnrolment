package com.example.clinicenrolment.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.clinicenrolment.login.LoginActivity
import com.google.gson.Gson
import java.util.ArrayList

class AccountManager(con: Context) : Session() {

    internal var accountManager: Session

    // myObject - instance of MyObject
   /* var loginUser: LoginUser
        get() {
            val gson = Gson()
            val json = accountManager.getString(loginUserStrinKey, "")
            return gson.fromJson(json, LoginUser::class.java)
        }
        set(userInfo) {
            val gson = Gson()
            val json = gson.toJson(userInfo)
            accountManager.putString(loginUserStrinKey, json)
        }
*/

    // myObject - instance of MyObject


    // myObject - instance of MyObject

    val userLogin: Boolean
        get() = accountManager.getBoolean(loginStatusKey, false)


     val userLoggenIn:String
        get() = accountManager.getString(userLoggedIn,"")

    init {
        accountManager = createSession(con, "AccountManager")
    }





    fun setUserLogin(value : Boolean) {
        accountManager.putBoolean(loginStatusKey, value)
    }

    fun setUserLoggedIn(value:String){

        accountManager.putString(userLoggedIn,value)
    }


    fun logoutUser() {
        accountManager.putBoolean(loginStatusKey, false)
        accountManager.putString(loginUserStrinKey, "")
    }




    fun logoutUserSuccessfully(contect:Activity)
    {
        val flagArrayList: ArrayList<Int> = ArrayList()

        flagArrayList.add(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        flagArrayList.add(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        flagArrayList.add(Intent.FLAG_ACTIVITY_NEW_TASK)

        launchActivityWithParameters(contect,LoginActivity::class.java,flagArrayList)

        logoutUser()
        setUserLogin(false)
        contect.finish()
    }


    fun launchActivityWithParameters(
        fromActivity: Context, toActivty:Class<*>,
        flags: ArrayList<Int>?
    ) {
        val intent = Intent(fromActivity, toActivty)
        //intent.putExtra("Bundle", bundle)
        if (flags != null) {
            if (!flags.isEmpty()) {
                for (i in flags.indices) {
                    intent.addFlags(flags[i])
                }
            }
        }
        fromActivity.startActivity(intent)
    }



    companion object {
        private val loginStatusKey = "isLogin"
        private val loginUserStrinKey = "loginUserString"
        private val userLoggedIn ="userLoggedIn"

    }
}