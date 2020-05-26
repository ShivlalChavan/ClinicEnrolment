package com.example.clinicenrolment.patientdetails

import android.app.Activity
import android.content.Intent
import android.media.MicrophoneInfo
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinicenrolment.R
import com.example.clinicenrolment.common.Connectivity
import com.example.clinicenrolment.common.ToolbarSetup

import com.example.clinicenrolment.model.*
import com.example.clinicenrolment.patientdetails.adapter.MedicineListAdapter
import com.example.clinicenrolment.reception.AddPatientResonActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_dashboard_layout.*
import kotlinx.android.synthetic.main.activity_patient_prescription.*
import kotlinx.android.synthetic.main.dailog_add_medicine.view.*
import org.json.JSONArray
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PateintPrescriptionActivity:AppCompatActivity()  , View.OnClickListener ,
              MedicineListAdapter.OnDeleteListener,MedicineListAdapter.OnEditListener , CompoundButton.OnCheckedChangeListener
{


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        when(buttonView!!.id)
        {
            R.id.switchBloodTest ->
            {
                if(isChecked){
                    TextInputEditText!!.visibility  = View.VISIBLE
                }
                else
                {
                    TextInputEditText!!.visibility  = View.GONE
                }
            }
        }


    }


    // AddPateintModel pojo

    private var coordinatorLayout:CoordinatorLayout?=null

    private var edtPatientName:EditText?=null
    private var edtPatientAge:EditText?=null
    private var edtPatientMoNo:EditText?=null
    private var edtReasonToMeet:EditText?=null
    private var edt_LabType:EditText?=null
    private var  llAddMedicine:LinearLayout?=null
    private var txt_Datepicker:TextView?=null

    private var btnSave: Button?=null

    private var patientObject : PatientDetail? = null

    private var medicineList :ArrayList<Medicine> = ArrayList()

    private lateinit  var medicineAdapter : MedicineListAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager

    private var  toolbar: androidx.appcompat.widget.Toolbar?=null

    private val REQUEST_CODE = 101
    private var PERSON_INTENT_INDEX :Int ?=null
    private var medicineobject : Medicine ?=null

    private lateinit var database: DatabaseReference

    private var nestedScrollView:NestedScrollView?=null

    private var llProgressBar : LinearLayout?=null

    private var reasonData :ArrayList<Reason> = ArrayList()

    private var switchBloodTest :Switch?=null

    private var TextInputEditText : TextInputLayout ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_prescription)


        patientObject  = intent.getSerializableExtra("patientObject") as? PatientDetail

        Log.e("GOt POJO ","Pateint detail"+patientObject?.apptDate)


        initViews()
    }

    private fun initViews() {

        database = FirebaseDatabase.getInstance().reference

        coordinatorLayout = findViewById(R.id.coordinatorLayout)as CoordinatorLayout

        toolbar = findViewById(R.id.toolbar)as Toolbar

        var toolbarSetup = ToolbarSetup(this,toolbar,"Add Medicine",R.drawable.ic_backicon)


        edtPatientName = findViewById<View>(R.id.edtPatientName)as EditText

        edtPatientAge = findViewById(R.id.edt_PatientAge)as EditText

        edtPatientMoNo = findViewById(R.id.edtMobileno)as EditText

        edtReasonToMeet = findViewById(R.id.edt_Reason)as EditText

        edt_LabType = findViewById(R.id.edt_LabType) as EditText

        txt_Datepicker = findViewById(R.id.txt_Datepicker)as TextView

        llAddMedicine = findViewById(R.id.llAddMedicine)as LinearLayout

        nestedScrollView=findViewById(R.id.llnested)as NestedScrollView


        llProgressBar = findViewById(R.id.llProgressBar) as LinearLayout

        switchBloodTest = findViewById(R.id.switchBloodTest) as Switch

        switchBloodTest!!.setOnCheckedChangeListener(this)

        TextInputEditText = findViewById(R.id.labTypeInput) as TextInputLayout

        btnSave = findViewById(R.id.btn_Save) as Button


        linearLayoutManager = LinearLayoutManager(this@PateintPrescriptionActivity)
        medicineRecyclerView.layoutManager = linearLayoutManager
        medicineRecyclerView.addItemDecoration(
            DividerItemDecoration(this@PateintPrescriptionActivity,
                DividerItemDecoration.VERTICAL)
        )

        // patientDataRecyclerView.adapter = PatientListAdapter(patientDataList,this@DashboardActivity)



        llAddMedicine!!.setOnClickListener(this)


        btnSave!!.setOnClickListener(this)




    }

    override fun onResume() {
        super.onResume()

        if(patientObject!=null)
        {
            llProgressBar!!.visibility = View.VISIBLE

            getReasonList()


        }
    }

    private fun getReasonList() {

       val list = database.child("PatientData")
                                   .child("reasonToMeet")
                                   .child("reasonList")


        val resonList = object: ValueEventListener {

            override fun onDataChange(data: DataSnapshot) {

                llProgressBar!!.visibility = View.GONE

                reasonData.clear()

                val sdf = SimpleDateFormat("dd/M/yyyy", Locale.US)
                val currentDate = sdf.format(Date())
                Log.e(" C DATE is :","-"+currentDate)

                for(list in data.children)
                {
                    if(list!=null)
                    {
                       // for(dataList : DataSnapshot in list.children )
                        //{
                            var reasonModel = list.getValue(Reason::class.java)

                            if (reasonModel!=null)
                            {
                                if(reasonModel?.pateingId!!.equals(patientObject!!.pateintId)
                                    && reasonModel?.apptmtDate!!.equals(currentDate)){

                                    var keyId = list.key
                                    reasonModel?.keyid = keyId!!

                                    reasonData.add(reasonModel!!)

                                    Log.e("RESON FOR ","PAtient : "+reasonModel?.reasontoMeet)

                                    updateFields()


                                }
                            }


                       // }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Log.e("in error","")
            }

        }

        list.addListenerForSingleValueEvent(resonList)


    }

    private fun updateFields() {


        Log.e("PATient","OBJECT "+patientObject!!.id)

        edtPatientName?.setText(patientObject!!.patientName)

        edtPatientAge?.setText(patientObject!!.patientAge)

        edtPatientMoNo?.setText(patientObject!!.patientMobileNo)

        var dateOfAppt =  StringBuilder().append("Date : ").append(patientObject?.apptDate)

        txt_Datepicker?.setText(dateOfAppt)


        if(reasonData!=null)
        {
            var apptReason  = reasonData.get(0).reasontoMeet
            edtReasonToMeet?.setText(apptReason)

        }


    }


    private fun sendMessage() {



        if( medicineList.size>0)
        {
            var gson = Gson()
           // gson.toJson(medicineList)

            var jsonArray : JSONArray = JSONArray(gson.toJson(medicineList))

            Log.e("JSON","LIST :"+jsonArray)

            var medicineMsg = StringBuilder()
            medicineMsg.append("* Medicine Details *").append("\n")

            for(i in 0 until jsonArray.length())
            {
                val objectJson = jsonArray.getJSONObject(i)

                 var medicineName = objectJson.get("medicineName")
                 var medicineQty = objectJson.get("medicineQty")

                var morning = objectJson.getBoolean("morningTime")
                var after = objectJson.getBoolean("afternoonTime")
                var night = objectJson.getBoolean("nightTime")
                var beforeMeal = objectJson.getBoolean("beforeMeal")
                var afterMeal = objectJson.getBoolean("aftermeal")



                medicineMsg.append("MedicineName: ").append(medicineName).append(" , Qty: ").append(medicineQty)
                            .append("\n")
                            //.append("---------------"+"\n")

               /* if(morning)
                {
                    medicineMsg.append("Morning ,")
                }

                if(after)
                {
                    medicineMsg.append("Afternoon ,")
                }

                if(night)
                {
                    medicineMsg.append("Night ,")
                }

                if(beforeMeal)
                {
                    medicineMsg.append(" BeforeMeal,"+"\n")
                }


                if(afterMeal)
                {
                    medicineMsg.append("AfterMeal,"+"\n")
                }

                medicineMsg.append("================="+"\n")*/

            }



            Log.e("MediCine","Mesage"+medicineMsg);


            var phoneNumber:String = patientObject!!.patientMobileNo

            try {

                val sms = SmsManager.getDefault()

               //  var parts : ArrayList<String> = ArrayList()
                 //parts = sms.divideMessage(medicineMsg.toString())


                sms.sendTextMessage("+91"+phoneNumber,null,medicineMsg.toString(),null,null)
              //  sms.sendMultipartTextMessage("+91"+phoneNumber,null,parts,null,null)

                Log.e("In SMS","DE:EVERD"+phoneNumber)
                medicineMsg.clear()

            } catch (e: Exception) {

                Log.e("Send","MessageException : "+e.printStackTrace())
            }

        }



    }


    override fun onClick(v: View?) {


        val itemId = v?.id

        when(itemId)
        {
            R.id.llAddMedicine-> addMedicineDailog()

            R.id.btn_Save ->
            {
                if(Connectivity.isConnected(this))
                {
                    savePatientPrescription()
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

    private fun savePatientPrescription() {


        if(medicineList!=null){


           // patientObject?.prescrition = medicineList

            val medicineBase = MedicineBase(medicineList)

            patientObject!!.isPrescribed = true

            var patientId = patientObject!!.pateintId

            if(switchBloodTest!!.isChecked)
            {
                patientObject!!.bloodTest = true

                var testType = edt_LabType!!.text.toString()
                patientObject!!.labTestType = testType

            }

            nestedScrollView!!.visibility = View.GONE
            llProgressBar!!.visibility = View.VISIBLE


            var apptReason = Reason()
            if(reasonData!=null)
            {
                apptReason  = reasonData.get(0)

                apptReason.isChecked  = true

            }


            database.child("PatientData").child("reasonToMeet").child("reasonList").child(apptReason!!.keyid).setValue(apptReason)

            database.child("PatientData").child("patientData").child("patientList").child(patientObject!!.id).setValue(patientObject)

            database.child("PatientData").child("patientMedicine").push().setValue(medicineList)

            sendMessage()

            nestedScrollView!!.visibility = View.VISIBLE
            llProgressBar!!.visibility = View.GONE

            //sendMessage()


            finish()
        }

        //patientObject?.prescrition?.addAll(medicineList)

        Log.e("Inside ","UPDATE")

    }


    fun  addMedicineDailog() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dailog_add_medicine, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val  mAlertDialog = mBuilder.show()


         var edtMedicineName: EditText? =  mAlertDialog.findViewById(R.id.edt_MedicineName)

         var edtMedicineQty :EditText? = mAlertDialog.findViewById(R.id.edt_Quantity)

         var checkMorning :CheckBox ?= mAlertDialog.findViewById(R.id.chkMorning)
         var checkBeforeMeal :CheckBox ?= mAlertDialog.findViewById(R.id.chkABeforeMeal)
         var  checkAfterMeal :CheckBox ?= mAlertDialog.findViewById(R.id.chkAfterMeal)
         var  checkAfternoon :CheckBox ?= mAlertDialog.findViewById(R.id.chkAfternoon)
         var  checkNight :CheckBox ?= mAlertDialog.findViewById(R.id.chkNight)



        mDialogView.btnAdd.setOnClickListener {
            //dismiss dialog

            var medicineName = edtMedicineName?.text.toString()
            var noOfMedicine = edtMedicineQty?.text.toString()

            var errorMsg = ""

            if(medicineName.isEmpty())
            {
                edtMedicineName!!.isFocusable = true
                if(errorMsg.equals(""))
                {
                    errorMsg = "Please enter Medicine name."
                }

            }

            if(noOfMedicine.isEmpty())
            {
                edtMedicineQty!!.isFocusable =  true
                if(errorMsg.equals(""))
                {
                    errorMsg = "Please enter Medicine Quantity."
                }
            }


            if (errorMsg.equals(""))
            {
                val  medicinePojo = Medicine();

                if(!medicineName.isNullOrEmpty()) {

                    medicinePojo.medicineName = medicineName
                }

                if(!noOfMedicine.isNullOrEmpty()) {

                    medicinePojo.medicineQty = noOfMedicine
                }


                if(checkMorning!!.isChecked)

                    medicinePojo.morningTime = true

                if(checkBeforeMeal!!.isChecked)

                    medicinePojo.beforeMeal = true


                if(checkAfterMeal!!.isChecked)

                    medicinePojo.aftermeal = true


                if(checkAfternoon!!.isChecked)

                    medicinePojo.afternoonTime = true


                if(checkNight!!.isChecked)

                    medicinePojo.nightTime = true

                 var resonListData =  reasonData.get(0)


                medicinePojo.patientId = patientObject?.pateintId

                medicinePojo.reasodId = resonListData.reasonId

                val sdf = SimpleDateFormat("dd/M/yyyy", Locale.US)
                val currentDate = sdf.format(Date())
                Log.e(" C DATE is :","-"+currentDate)

                medicinePojo.medicineDate = currentDate

                medicinePojo?.let { it1 -> medicineList?.add(it1) }


                medicineAdapter = MedicineListAdapter(medicineList,this@PateintPrescriptionActivity,this,this)
                medicineRecyclerView.adapter = medicineAdapter



                mAlertDialog.dismiss()
            }
            else
            {
                val snackbar = Snackbar.make(mDialogView, errorMsg,
                    Snackbar.LENGTH_LONG)
                snackbar.show()

                errorMsg=""


            }


            /* medicineAdapter.onItemClick={ pos, view ->

                 Log.e("Image","click"+view.getId()+":"+pos)
             }*/

          //  patientObject?.prescrition = medicineList

           // mAlertDialog.dismiss()

        }
        //cancel button click of custom layout
        mDialogView.btnClose.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }

    private fun onItemClicked(patientItem: Medicine) {

        Log.e("item","clicked"+patientItem.medicineName);


       /* val showDetailActivityIntent = Intent(this, PateintPrescriptionActivity::class.java)

        // showDetailActivityIntent.putExtra("ItemId", partItem.id)
        showDetailActivityIntent.putExtra("patientObject",patientItem as Serializable)


        startActivity(showDetailAcvityIntent)*/

    }


    override fun editItem(pojo: Medicine, indexx: Int) {

        var gson = Gson()
        Log.e("Got","POSITION:"+indexx +"/"+gson.toJson(pojo))

        PERSON_INTENT_INDEX = indexx
        val intent = Intent(this@PateintPrescriptionActivity, EditMedicineActivity::class.java)
         intent.putExtra("itemPosition", indexx)
        intent.putExtra("medicineObject",pojo as Serializable)
        startActivityForResult(intent, REQUEST_CODE)



    }

    override fun deleteItem(pojo: Medicine) {

        medicineAdapter.dataList.remove(pojo)
        medicineAdapter.notifyDataSetChanged()



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode== REQUEST_CODE){

            if(resultCode == Activity.RESULT_OK && data != null){

                val bundle = data.extras


                    val index:Int = data.getIntExtra("itemPosition", 0)
                    // something very wrong occurred, do nothing

                medicineobject = data.getSerializableExtra("medicineObject") as Medicine?
                    if (index == -1)
                        return

                medicineobject?.let { medicineAdapter.dataList.set(index, it) }
                    medicineAdapter.notifyItemChanged(index)

                }else{
                    /*medicineobject?.let { medicineAdapter.dataList.add(it) }
                    medicineAdapter.notifyItemInserted(medicineAdapter.dataList.size-1)*/
                }
            }

        }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.previous_report,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home ->
            {
                finish()
            }

           R.id.view_report ->
           {
               val showDetailActivityIntent = Intent(this, PatientDocumentActivity::class.java)
               // showDetailActivityIntent.putExtra("ItemId", partItem.id)
               showDetailActivityIntent.putExtra("patientObject",patientObject as Serializable)

               startActivity(showDetailActivityIntent)


           }
            R.id.view_medicine ->
            {
                val showDetailActivityIntent = Intent(this, PatientReportActivity::class.java)

                // showDetailActivityIntent.putExtra("ItemId", partItem.id)
                showDetailActivityIntent.putExtra("patientObject",patientObject as Serializable)

                startActivity(showDetailActivityIntent)


            }



        }
        return super.onOptionsItemSelected(item)
    }




    }






