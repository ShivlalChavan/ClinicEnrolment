package com.example.clinicenrolment.reception

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.clinicenrolment.R
import com.example.clinicenrolment.common.Connectivity
import com.example.clinicenrolment.common.ToolbarSetup
import com.example.clinicenrolment.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddPatientResonActivity :AppCompatActivity() , View.OnClickListener
{


    private var edtPatientName:EditText?=null
    private var edtPatientAge:EditText?=null
    private var edtPatientMoNo:EditText?=null
    private var edtReasonToMeet:EditText?=null
    private var txtDateOfAppt:TextView?=null

    private var btnSave:Button?=null

    private lateinit var database: DatabaseReference


    private var patientName:String?=""
    private var patientMoNo:String?=""
    private var patientAge:String?=""
    private  var reason:String?=""
    private var globalErrorMessage:String?=""

    private var llProgressBar : LinearLayout?=null
    private var  toolbar: androidx.appcompat.widget.Toolbar?=null


    private var patientObject : PatientDetail? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addpatient_layout)

        patientObject  = intent.getSerializableExtra("patientObject") as? PatientDetail

        Log.e("GOt POJO ","Pateint detail"+patientObject?.apptDate)


        initViews()
    }

    private fun initViews() {

        database = FirebaseDatabase.getInstance().reference

        edtPatientName=findViewById<View>(R.id.edtPatientName)as EditText

        edtPatientAge = findViewById(R.id.edt_PatientAge)as EditText

        edtPatientMoNo = findViewById(R.id.edtMobileno)as EditText

        edtReasonToMeet = findViewById(R.id.edt_Reason)as EditText

        txtDateOfAppt = findViewById(R.id.txt_Datepicker) as TextView

        btnSave = findViewById(R.id.btn_Save) as Button

        llProgressBar = findViewById(R.id.llProgressBar) as LinearLayout

        toolbar = findViewById(R.id.toolbar)as Toolbar

        var toolbarSetup = ToolbarSetup(this,toolbar,"Add Pateint",R.drawable.ic_backicon)

        btnSave!!.setOnClickListener(this)

        txtDateOfAppt!!.setOnClickListener(this)



    }

    override fun onResume() {
        super.onResume()

        if(patientObject!=null)
        {
            edtPatientName?.setText(patientObject!!.patientName)

            edtPatientAge?.setText(patientObject!!.patientAge)

            edtPatientMoNo?.setText(patientObject!!.patientMobileNo)



        }
    }



    override fun onClick(v: View?) {


        val itemId= v?.id

        when(itemId)
        {

            R.id.btn_Save->
            {
                if(Connectivity.isConnected(this))
                {
                    savePatientDetails()
                }
                else
                {
                    val snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.internet_connectivity),
                        Snackbar.LENGTH_LONG)
                    snackbar.show()
                }



            }


            R.id.txt_Datepicker-> txtDateOfAppt?.let { changeDate(it) }



        }








    }


    fun changeDate(view: View) {

        var date: Calendar = Calendar.getInstance()
        var thisAYear = date.get(Calendar.YEAR).toInt()
        var thisAMonth = date.get(Calendar.MONTH).toInt()
        var thisADay = date.get(Calendar.DAY_OF_MONTH).toInt()

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view2, thisYear, thisMonth, thisDay ->
            // Display Selected date in textbox
            var cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, thisYear)
            cal.set(Calendar.MONTH, thisMonth)
            cal.set(Calendar.DAY_OF_MONTH, thisDay)

            thisAMonth = thisMonth
            thisADay = thisDay
            thisAYear = thisYear

            val myFormat = "dd/M/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)



            //txtDateOfAppt?.setText(""+thisDay  + "/" + thisAMonth+ "/" + thisYear)

            txtDateOfAppt?.text = sdf.format(cal.time)

            val newDate:Calendar = Calendar.getInstance()


            newDate.set(thisYear, thisMonth, thisDay)

        }, thisAYear, thisAMonth, thisADay)

        dpd.datePicker.minDate = (System.currentTimeMillis()-1000)
        dpd.show()


      //  mh.entryDate = date.timeInMillis
       /* println("DATE DATA: "+thisAYear+ " "+thisAMonth+" " + thisADay)
        println("DATE CHANGED MILLISECS = "+mh.entryDate)*/
    }

    private fun savePatientDetails() {


        patientName = edtPatientName?.text.toString()
        patientMoNo = edtPatientMoNo?.text.toString()
        patientAge = edtPatientAge?.text.toString()
        reason = edtReasonToMeet?.text.toString()

        var  date = txtDateOfAppt?.text.toString()


        if(patientName.equals("")){

            if(globalErrorMessage.equals("")){

                globalErrorMessage ="Please enter name."
                edtPatientName?.requestFocus()
                edtPatientName?.isFocusableInTouchMode = true

            }
        }


        if(patientMoNo.equals("") || patientMoNo!!.length<10 ){

            if(patientMoNo.equals(""))
            {
                if(globalErrorMessage.equals("")){

                    globalErrorMessage ="Please enter mobile number."
                    edtPatientMoNo?.requestFocus()
                    edtPatientMoNo?.isFocusableInTouchMode = true

                }
            }
            else
            {
                if(globalErrorMessage.equals("")){

                    globalErrorMessage ="Please enter valid  mobile number."
                    edtPatientMoNo?.requestFocus()
                    edtPatientMoNo?.isFocusableInTouchMode = true

                }

            }


        }


        if(patientAge.equals("")){

            if(globalErrorMessage.equals("")){

                globalErrorMessage ="Please enter age."
                edtPatientAge?.requestFocus()
                edtPatientAge?.isFocusableInTouchMode = true

            }
        }


        if(reason.equals("")){

            if(globalErrorMessage.equals("")){

                globalErrorMessage ="Please enter reason to meet."
                edtReasonToMeet?.requestFocus()
                edtReasonToMeet?.isFocusableInTouchMode = true

            }
        }

        if(date.equals("Date :")){

            if(globalErrorMessage.equals("")){

                globalErrorMessage ="Please select date."

            }
        }





        if (Connectivity.isConnected(this))
        {
            if (globalErrorMessage.equals("")) {

                llProgressBar!!.visibility = View.VISIBLE

                var list:ArrayList<Medicine> = ArrayList()


               // val patientId = database.child("PatientDetail").child("").push().key

               /* var patientData= PatientDetail(patientObject!!.id,patientObject!!.pateintId,patientName.toString(),
                    patientAge.toString(),patientMoNo.toString(),reason.toString(),date)
    */
                patientObject!!.patientName = patientName.toString()

                patientObject!!.patientAge = patientAge.toString()

                patientObject!!.patientMobileNo = patientMoNo.toString()

                patientObject!!.apptDate = date

               /* var patientList : ArrayList<PatientDetail> = ArrayList()

                patientList.add(patientData)
    */
                var reasonListData :ArrayList<Reason> = ArrayList()

                val reasonId = database.child("PatientData").child("reasonToMeet").child("reasonList").push().key

                var reasonData = Reason(reasonId.toString(),patientObject!!.pateintId,reason.toString(),date)

                reasonListData.add(reasonData)


               //  var patientBase = PatientBase(patientList)
                 var reasonBase = ReasonBase(reasonListData)

                // for iitialization

                var medicine :ArrayList<Medicine> = ArrayList()
                var medicineBase = MedicineBase(medicine)



               // val patientDetail = AddPateintModel(patientBase,medicineBase,reasonBase)

              //  val key = database.child("PatientData").push().key

                //patientDetail.pateintId = key!!
              //  database.child("PatientData").child("patientData").child("patientList").push().setValue(patientData)

               // database.child("PatientData").setValue(patientDetail)

                database.child("PatientData").child("patientData").child("patientList").child(patientObject!!.id).setValue(patientObject)

                database.child("PatientData").child("reasonToMeet").child("reasonList").push().setValue(reasonData)

                Log.e("Inside ","adddatabase")

                Log.e("added","new values"+patientName+patientMoNo+patientAge+reason)

                clearFields()

                llProgressBar!!.visibility = View.GONE

                finish()
            }
            else
            {
                val snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), globalErrorMessage.toString(),
                    Snackbar.LENGTH_LONG)
                snackbar.show()
                globalErrorMessage=""

            }
        } else {

            val snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.internet_connectivity),
                Snackbar.LENGTH_LONG)
            snackbar.show()
        }


    }

    private fun clearFields() {

        edtPatientName?.setText("")
        edtPatientMoNo?.setText("")
        edtPatientAge?.setText("")
        edtReasonToMeet?.setText("")
        txtDateOfAppt?.setText("Date :")

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