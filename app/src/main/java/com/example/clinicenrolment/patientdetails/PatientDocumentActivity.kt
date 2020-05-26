package com.example.clinicenrolment.patientdetails

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinicenrolment.R
import com.example.clinicenrolment.common.Connectivity
import com.example.clinicenrolment.common.ToolbarSetup
import com.example.clinicenrolment.model.DocumentModel
import com.example.clinicenrolment.model.Medicine
import com.example.clinicenrolment.model.PatientDetail
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

import kotlin.collections.ArrayList
import kotlin.text.StringBuilder

class PatientDocumentActivity : AppCompatActivity() , View.OnClickListener {



    private var toolbar:Toolbar?=null
    private var llProgressBar: LinearLayout?=null
    private var llDocument:LinearLayout?=null

    private var llEmpty : LinearLayout?=null

    private var txtEmptyData : TextView ?= null

    private var coordinatorLayout: CoordinatorLayout? = null



    private var documentlist:ArrayList<DocumentModel> = ArrayList()


    private lateinit var linearLayoutManager: LinearLayoutManager

    private var patientObject : PatientDetail? = null

    private lateinit var database: DatabaseReference


    private var txtDocumentName:TextView?=null
    private var txtDate:TextView?=null

    private var edtComment:EditText?=null
    private var btnSubmit: Button?=null

    private var imageDocument:ImageView?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_patient_document_layout)


        patientObject  = intent.getSerializableExtra("patientObject") as? PatientDetail

        Log.e("GOt POJO ","Pateint detail"+patientObject?.apptDate)




         initViews()
    }

    private fun initViews() {

        coordinatorLayout = findViewById(R.id.coordinatorLayout) as CoordinatorLayout

        toolbar = findViewById(R.id.toolbar)as Toolbar

        var toolbarSetup = ToolbarSetup(this,toolbar,"Lab Report",R.drawable.ic_backicon)


        database = FirebaseDatabase.getInstance().reference

        llProgressBar = findViewById(R.id.llProgressBar) as LinearLayout

        imageDocument = findViewById(R.id.imageDocument) as ImageView

        txtDocumentName = findViewById(R.id.txtDocumentName) as TextView

        txtDate = findViewById(R.id.txtDate) as TextView

        llEmpty = findViewById(R.id.llEmpty) as LinearLayout

        llDocument = findViewById(R.id.llDocument) as LinearLayout

        txtEmptyData = findViewById(R.id.txtEmptyData) as TextView
    }

    override fun onResume() {
        super.onResume()

        if (Connectivity.isConnected(this)) {
            if(patientObject!=null)
            {

                getDocumentList(patientObject!!)

                llProgressBar!!.visibility = View.VISIBLE


            }
        } else
        {
            val snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.internet_connectivity),
                Snackbar.LENGTH_LONG)
            snackbar.show()
        }

    }

    private fun getDocumentList(patientObject: PatientDetail) {

        var medicineListRef = database.child("PatientData")
                                             .child("patientDocument").child("documentList")

        val list = object:ValueEventListener{

            override fun onDataChange(data: DataSnapshot) {

                documentlist.clear()

                for(list in data.children)
                {
                    if(list!=null)
                    {
                        // for(dataList :DataSnapshot in list.children)
                         //{
                            // for(medicinedata :DataSnapshot in dataList.children)
                             //{
                                 var documentPojo = list.getValue(DocumentModel::class.java)

                                 if(documentPojo?.patientId!!.equals(patientObject.pateintId))
                                 {
                                     documentlist.add(documentPojo)

                                 }

                             //}

                        // }

                    }
                }

               if(documentlist!=null && documentlist.size > 0)
               {
                   llProgressBar?.visibility = View.GONE
                   llDocument?.visibility = View.VISIBLE

                   llEmpty?.visibility = View.GONE
                   txtEmptyData?.visibility = View.GONE


                   setupDataList(documentlist)

               }else
               {
                   llProgressBar?.visibility = View.GONE
                   llDocument?.visibility = View.GONE

                   llEmpty?.visibility = View.VISIBLE

                   txtEmptyData?.visibility = View.VISIBLE
                   //txtEmptyData?.visibility = View.VISIBLE
               }



            }


            override fun onCancelled(error: DatabaseError) {

                Log.e("in error","medicineList"+error.message)
            }



        }


        medicineListRef.addListenerForSingleValueEvent(list)




    }

    private fun setupDataList(documentlist: ArrayList<DocumentModel>) {

       // llProgressBar!!.visibility = View.GONE



        var pojo : DocumentModel = documentlist.get(0)

        var picPath = pojo.documentPath

        Picasso.get().load(picPath).into(imageDocument)

        var bindString:String = StringBuilder().append(" Document Name : ").append(pojo.documentName).toString()

        txtDocumentName!!.setText(bindString)

        var finalDate:String = StringBuilder().append(" Date : ").append(pojo.date).toString()

        txtDate!!.setText(finalDate)







    }


    override fun onClick(v: View?) {



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