package com.example.clinicenrolment.searchpatient

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinicenrolment.R
import com.example.clinicenrolment.common.Connectivity
import com.example.clinicenrolment.common.ToolbarSetup
import com.example.clinicenrolment.dashboard.adapter.PatientListAdapter
import com.example.clinicenrolment.model.AddPateintModel
import com.example.clinicenrolment.model.Medicine
import com.example.clinicenrolment.model.PatientDetail
import com.example.clinicenrolment.patientdetails.PateintPrescriptionActivity
import com.example.clinicenrolment.patientdetails.adapter.MedicineListAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_dashboard_layout.*
import kotlinx.android.synthetic.main.activity_patient_prescription.*
import kotlinx.android.synthetic.main.activity_search_patient_layout.*
import kotlinx.android.synthetic.main.dailog_add_medicine.view.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SearchPateintActivity:AppCompatActivity()  , View.OnClickListener
{


    // AddPateintModel pojo

    private var edtPatientName:EditText?=null
    private var txtEmptyData:TextView?=null


    private val patientDataList:ArrayList<PatientDetail> = ArrayList()

    private val filterDataList:ArrayList<PatientDetail> = ArrayList()
    private  lateinit var adapter: PatientListAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager

    private var  toolbar: androidx.appcompat.widget.Toolbar?=null


    private lateinit var database: DatabaseReference

    private var nestedScrollView:NestedScrollView?=null

    private var llProgressBar : LinearLayout?=null
    internal var textlength = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_patient_layout)


        initViews()
    }

    private fun initViews() {

        database = FirebaseDatabase.getInstance().reference



        toolbar = findViewById(R.id.toolbar)as Toolbar

        var toolbarSetup = ToolbarSetup(this,toolbar,"Add Medicine",R.drawable.ic_backicon)


        edtPatientName = findViewById<View>(R.id.edtPatientName)as EditText



        llProgressBar = findViewById(R.id.llProgressBar) as LinearLayout

        txtEmptyData = findViewById(R.id.txtEmptyData)as TextView


      //  nestedScrollView=findViewById(R.id.llnested)as NestedScrollView


        llProgressBar = findViewById(R.id.llProgressBar) as LinearLayout

        linearLayoutManager = LinearLayoutManager(this@SearchPateintActivity)
        patientDataRecyclerView.layoutManager = linearLayoutManager
        patientDataRecyclerView.addItemDecoration(
            DividerItemDecoration(this@SearchPateintActivity,
                DividerItemDecoration.VERTICAL)
        )


        edtPatientName!!.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textlength = edtPatientName!!.text.length

                filterDataList.clear()

                 for(i in patientDataList.indices){

                    if(textlength <= patientDataList[i].patientName.length){

                        if(patientDataList[i].patientName.toLowerCase().trim().contains(
                                edtPatientName!!.text.toString().toLowerCase().trim(){it <= ' ' }
                            ))
                        {
                                 filterDataList.add(patientDataList[i])
                        }
                    }
                 }
                  if(filterDataList.size!=0)
                  {
                      adapter = PatientListAdapter(filterDataList,{  patientItem: PatientDetail-> onItemClicked(patientItem) },this@SearchPateintActivity )
                      patientDataRecyclerView.adapter = adapter

                  }
                else
                  {
                      patientDataRecyclerView.visibility = View.GONE
                      txtEmptyData!!.visibility = View.VISIBLE
                  }
            }
        })


    }

    override fun onResume() {
        super.onResume()

        if(Connectivity.isConnected(this))
        {
            populateList()
        }
        else
        {
            val snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.internet_connectivity),
                Snackbar.LENGTH_LONG)
            snackbar.show()
        }

    }

    private fun populateList() {


        llProgressBar!!.visibility = View.VISIBLE

        val list = database.child("PatientData").child("patientData").child("patientList")

        val patientList = object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {

                llProgressBar!!.visibility = View.GONE

                patientDataList.clear()

                val sdf = SimpleDateFormat("dd/M/yyyy", Locale.US)
                val currentDate = sdf.format(Date())
                Log.e(" C DATE is :","-"+currentDate)

                for (productSnapshot in data.children) {
                    val product = productSnapshot.getValue(PatientDetail::class.java)

                    patientDataList.add(product!!)

                }
                  Log.e("got ","list"+data.value)

                if(patientDataList.size!=0){


                    linearLayoutManager = LinearLayoutManager(this@SearchPateintActivity)
                    patientDataRecyclerView.layoutManager = linearLayoutManager
                    // patientDataRecyclerView.addItemDecoration(DividerItemDecoration(this@DashboardActivity,DividerItemDecoration.VERTICAL))

                    // patientDataRecyclerView.adapter = PatientListAdapter(patientDataList,this@DashboardActivity)

                    adapter = PatientListAdapter(patientDataList,{  patientItem: PatientDetail-> onItemClicked(patientItem) },this@SearchPateintActivity )
                    patientDataRecyclerView.adapter = adapter


                }
                else
                {
                    patientDataRecyclerView.visibility = View.GONE
                    txtEmptyData?.visibility = View.VISIBLE
                }



            }

            override fun onCancelled(dataError: DatabaseError) {

            }


        }

        list.addListenerForSingleValueEvent(patientList)

    }


    override fun onClick(v: View?) {

    }





    private fun onItemClicked(patientItem: PatientDetail) {

        val showDetailActivityIntent = Intent(this, PateintPrescriptionActivity::class.java)

        // showDetailActivityIntent.putExtra("ItemId", partItem.id)
        showDetailActivityIntent.putExtra("patientObject",patientItem as Serializable)


        startActivity(showDetailActivityIntent)

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






