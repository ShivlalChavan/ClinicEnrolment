package com.example.clinicenrolment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.example.clinicenrolment.common.AccountManager
import com.example.clinicenrolment.dashboard.DashboardActivity
import com.example.clinicenrolment.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    private var mAuth: FirebaseAuth? = null

    private var accntManager : AccountManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_NO_TITLE)
        //making this activity full screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)



        accntManager = AccountManager(this)



        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    override fun onStart() {
        super.onStart()

    }


    internal val mRunnable: Runnable = Runnable {

        if (!isFinishing) {

            if(accntManager!!.userLogin == false){

                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()

            }
            else if(accntManager!!.userLogin == true)
            {

                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()

            }





        }
    }

}
