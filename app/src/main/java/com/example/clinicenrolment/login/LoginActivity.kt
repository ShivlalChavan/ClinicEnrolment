package com.example.clinicenrolment.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.clinicenrolment.R
import com.example.clinicenrolment.common.AccountManager
import com.example.clinicenrolment.common.Connectivity
import com.example.clinicenrolment.dashboard.DashboardActivity
import com.example.clinicenrolment.reception.AddPatientDetailActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity: AppCompatActivity() ,View.OnClickListener{

    private var coordinatorLayout:CoordinatorLayout?=null
    private var edtUserName:EditText?=null
    private var edtPassword:EditText?=null
    private var btnLogin:Button?=null


    //Firebase reference
    private var mAuth: FirebaseAuth? = null

    //global variables
    private var email: String? = null
    private var password: String? = null

    private var globalErrorMessage:String?=""

    private var accountManager : AccountManager?=null

    private var llProgressBar : LinearLayout?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_NO_TITLE)
        //making this activity full screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_login_layout)

        initViews()

    }

    private fun initViews() {

        accountManager = AccountManager(this)

        coordinatorLayout=findViewById(R.id.coordinatorLayout)as CoordinatorLayout

        edtUserName=findViewById<View>(R.id.edtUserName)as EditText
        edtPassword=findViewById(R.id.edtPassword) as EditText

        btnLogin=findViewById(R.id.btnLogin) as Button

        llProgressBar = findViewById(R.id.llProgressBar) as LinearLayout

        btnLogin!!.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()



    }



    override fun onClick(v: View) {
        val item_id = v.id
        when (item_id) {

            R.id.btnLogin->
            {
                if(Connectivity.isConnected(this))
                {
                    userLogin()
                }
                else
                {
                    val snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.internet_connectivity),
                        Snackbar.LENGTH_LONG)
                    snackbar.show()

                }

            }

        }
    }


    private fun userLogin(){

        email = edtUserName?.text.toString()
        password = edtPassword?.text.toString()


        Log.e("Inside ","Login")


        if(email.equals("")){

            if(globalErrorMessage.equals("")){

                globalErrorMessage ="Please enter username"
                edtUserName?.requestFocus()
                edtUserName?.isFocusableInTouchMode = true

            }
        }

        if(password.equals("")){

            if(globalErrorMessage.equals("")){

                globalErrorMessage ="Please enter password"
                edtPassword?.requestFocus()
                edtPassword?.isFocusableInTouchMode = true

            }
        }


        if(globalErrorMessage.equals("")){


            llProgressBar!!.visibility = View.VISIBLE


            this.mAuth?.signInWithEmailAndPassword(email!!,password!!)?.addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->

                if (task.isSuccessful) {

                    llProgressBar!!.visibility = View.GONE

                    Log.e("LOGGED","IN"+task.result)

                    accountManager!!.setUserLogin(true)

                    accountManager!!.setUserLoggedIn(email!!)

                   // Toast.makeText(this, "Successfully Logged in :)", Toast.LENGTH_LONG).show()

                    val user = mAuth!!.currentUser

                    val  intent=Intent(this, DashboardActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)


                    /* if(user!!.email.equals("reception@gmail.com"))
                     {
                         val  intent=Intent(this, AddPatientDetailActivity::class.java)
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
                         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                         startActivity(intent)
                         finish()
                     }
                     else
                     {
                         val  intent=Intent(this, DashboardActivity::class.java)
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
                         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                         startActivity(intent)

                     }
 */



                }
                else
                {

                    llProgressBar!!.visibility = View.GONE
                   // Toast.makeText(this, "Error Logging in :(", Toast.LENGTH_SHORT).show()
                    var exception = task.exception?.message.toString()

                    val snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), exception,
                        Snackbar.LENGTH_LONG)
                    snackbar.show()


                    Log.e("Error","Login"+task.exception)
                }
            })


        }
        else
        {

            val snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), globalErrorMessage.toString(),
                Snackbar.LENGTH_LONG)
            snackbar.show()
            globalErrorMessage=""
        }

    }
}