package com.example.clinicenrolment.medical

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicenrolment.R
import com.example.clinicenrolment.common.Connectivity
import com.example.clinicenrolment.common.ToolbarSetup
import com.example.clinicenrolment.dashboard.adapter.PatientListAdapter
import com.example.clinicenrolment.medical.adapter.PatientMedicineListAdapter
import com.example.clinicenrolment.model.Medicine
import com.example.clinicenrolment.model.PatientDetail
import com.example.clinicenrolment.patientdetails.adapter.MedicineListAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_patient_prescription.*
import kotlinx.android.synthetic.main.activity_patient_prescription.coordinatorLayout
import kotlinx.android.synthetic.main.activity_patientmedicine_layout.*
import kotlinx.android.synthetic.main.fragment_home_layout.*

class PatientMedicinesActivity : AppCompatActivity() , View.OnClickListener {



    private var toolbar:Toolbar?=null
    private var llProgressBar: LinearLayout?=null
    private var llMedicineList:LinearLayout?=null

    private var coordinatorLayout: CoordinatorLayout? = null



    private var medicinelist:ArrayList<Medicine> = ArrayList()

    private var medicineRecyclerView :RecyclerView?=null

    private lateinit  var medicineAdapter : PatientMedicineListAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager

    private var patientObject : PatientDetail? = null

    private lateinit var database: DatabaseReference


    private var txtEmptyData:TextView?=null

    private var edtComment:EditText?=null
    private var btnSubmit: Button?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_patientmedicine_layout)


        patientObject  = intent.getSerializableExtra("patientObject") as? PatientDetail

        Log.e("GOt POJO ","Pateint detail"+patientObject?.apptDate)




         initViews()
    }

    private fun initViews() {

        coordinatorLayout = findViewById(R.id.coordinatorLayout) as CoordinatorLayout

        toolbar = findViewById(R.id.toolbar)as Toolbar

        var toolbarSetup = ToolbarSetup(this,toolbar,"Medicines",R.drawable.ic_backicon)


        database = FirebaseDatabase.getInstance().reference

        llProgressBar = findViewById(R.id.llProgressBar) as LinearLayout

        txtEmptyData = findViewById(R.id.txtEmptyData)as TextView


        toolbar = findViewById<Toolbar>(R.id.toolbar)

        llProgressBar = findViewById(R.id.llProgressBar) as LinearLayout

        llMedicineList = findViewById(R.id.llMedicineList) as LinearLayout

        medicineRecyclerView = findViewById(R.id.medicineRecyclerView)as RecyclerView

        edtComment = findViewById(R.id.edtComment)as EditText

        btnSubmit = findViewById(R.id.btnSubmit)as Button

       /* linearLayoutManager = LinearLayoutManager(this@PatientMedicinesActivity)
        medicineRecyclerView.layoutManager = linearLayoutManager
        medicineRecyclerView.addItemDecoration(
            DividerItemDecoration(this@PatientMedicinesActivity,
                DividerItemDecoration.VERTICAL)
        )*/



        btnSubmit!!.setOnClickListener(this)


    }

    override fun onResume() {
        super.onResume()

        if (Connectivity.isConnected(this)) {
            if(patientObject!=null)
            {

                getMedicineList(patientObject!!)
            }
        } else {

            val snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.internet_connectivity),
                Snackbar.LENGTH_LONG)
            snackbar.show()
        }

    }

    private fun getMedicineList(patientObject: PatientDetail) {

        var medicineListRef = database.child("PatientData")
                                             .child("patientMedicine")

        val list = object:ValueEventListener{

            override fun onDataChange(data: DataSnapshot) {

                medicinelist.clear()

                for(list in data.children)
                {
                    if(list!=null)
                    {
                         for(dataList :DataSnapshot in list.children)
                         {
                            // for(medicinedata :DataSnapshot in dataList.children)
                             //{
                                 var medicinePojo = dataList.getValue(Medicine::class.java)

                                 if(medicinePojo?.patientId!!.equals(patientObject.pateintId))
                                 {
                                     medicinelist.add(medicinePojo)

                                 }

                             //}

                         }

                    }
                }

               if(medicinelist!=null)
               {
                   setupDataList(medicinelist)

               }else
               {
                   llMedicineList?.visibility = View.GONE
                   txtEmptyData?.visibility = View.VISIBLE
               }



            }


            override fun onCancelled(error: DatabaseError) {

                Log.e("in error","medicineList"+error.message)
            }



        }


        medicineListRef.addListenerForSingleValueEvent(list)




    }

    private fun setupDataList(medicinelist: ArrayList<Medicine>) {

        if(medicinelist!=null)
        {
            llMedicineList?.visibility = View.VISIBLE
            txtEmptyData?.visibility = View.GONE

            linearLayoutManager = LinearLayoutManager(this@PatientMedicinesActivity)
            medicineRecyclerView?.layoutManager = linearLayoutManager

            medicineRecyclerView?.addItemDecoration(DividerItemDecoration(this@PatientMedicinesActivity,DividerItemDecoration.VERTICAL))

            //  patientDataRecyclerView.adapter = PatientListAdapter(patientDataList,activity)

            medicineAdapter = PatientMedicineListAdapter(medicinelist,this@PatientMedicinesActivity )
            medicineRecyclerView?.adapter = medicineAdapter

        }
        else
        {
            llMedicineList?.visibility = View.GONE
            txtEmptyData?.visibility = View.VISIBLE
        }




    }


    override fun onClick(v: View?) {

        when(v!!.id)
        {
            R.id.btnSubmit->{

                if (Connectivity.isConnected(this)) {
                    var comment = edtComment!!.text.toString()

                    if(!comment.isEmpty())
                    {
                        patientObject?.comment = comment

                    }

                    patientObject?.isTakenMedicine = true


                    database.child("PatientData").child("patientData").child("patientList").child(patientObject!!.id).setValue(patientObject)

                    edtComment?.setText("")

                    finish()
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home ->
            {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}