package com.example.clinicenrolment.patientdetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.clinicenrolment.R
import com.example.clinicenrolment.model.AddPateintModel
import com.example.clinicenrolment.model.Medicine
import java.io.Serializable

class EditMedicineActivity : AppCompatActivity()  , View.OnClickListener {

  private var medicineobject : Medicine ?=null

    private var edtMedicineName: EditText?=null
    private var edtMedicineNO: EditText?=null
    private var chkBeforeMeal :CheckBox?= null
    private var chkAfterMeal :CheckBox?= null
    private var chkMorning :CheckBox?= null
    private var chkAfternoon :CheckBox?= null
    private var chkNight :CheckBox?= null
    private var btnAdd:Button?=null
    private var btnClose:Button?=null

    private var position =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_medicine)

        medicineobject  = intent.getSerializableExtra("medicineObject") as? Medicine

        position = intent.getIntExtra("itemPosition",0)

        Log.e("GOt POJO ","Medicine detail"+medicineobject.toString()+"posotiom "+position)


        initView()


    }

    private fun initView() {

        edtMedicineName=findViewById<EditText>(R.id.edt_MedicineName)


        edtMedicineNO =findViewById<EditText>(R.id.edt_Quantity)

        chkBeforeMeal = findViewById<CheckBox>(R.id.chkABeforeMeal)

        chkAfterMeal = findViewById<CheckBox>(R.id.chkAfterMeal)

        chkMorning = findViewById<CheckBox>(R.id.chkMorning)

        chkAfternoon = findViewById<CheckBox>(R.id.chkAfternoon)

        chkNight = findViewById<CheckBox>(R.id.chkNight)

        btnAdd = findViewById<Button>(R.id.btnAdd)

        btnClose = findViewById<Button>(R.id.btnClose)

        btnAdd!!.setOnClickListener(this)

        btnClose!!.setOnClickListener(this)



    }


    override fun onResume() {
        super.onResume()

        if(medicineobject!=null)
        {

            if (medicineobject!!.medicineName?.isNullOrEmpty()) {

                edtMedicineName?.setText( medicineobject!!.medicineName)
            }

            if(medicineobject!!.medicineQty?.isNullOrEmpty())
            {
                edtMedicineNO?.setText( medicineobject!!.medicineQty)
            }

            if(medicineobject!!.beforeMeal)
            {

                chkBeforeMeal!!.isChecked = true
            }


            if(medicineobject!!.aftermeal)
            {

                chkAfterMeal!!.isChecked = true
            }



            if(medicineobject!!.morningTime)
            {

                chkMorning!!.isChecked = true
            }


            if(medicineobject!!.afternoonTime)
            {

                chkAfternoon?.isChecked = true
            }


            if(medicineobject!!.nightTime)
            {

                chkNight?.isChecked = true
            }



        }

    }




    override fun onClick(v: View?) {

        val itemId= v?.id

        when(itemId)
        {

            R.id.btnAdd-> saveEdited()


            R.id.btnClose ->closeActivity()

        }

    }

    private fun closeActivity() {


        setResult(Activity.RESULT_CANCELED)
        finish()

    }

    private fun saveEdited() {


        var medicineName = edtMedicineName?.text.toString()
        var noOfMedicine = edtMedicineNO?.text.toString()

        if(!medicineName?.isNullOrEmpty()) {

            medicineobject?.medicineName = medicineName
        }

        if(!noOfMedicine?.isNullOrEmpty()) {

            medicineobject?.medicineQty = noOfMedicine
        }


        if(chkMorning!!.isChecked)

            medicineobject?.morningTime = true

        if(chkBeforeMeal!!.isChecked)

            medicineobject?.beforeMeal = true


        if(chkAfterMeal!!.isChecked)

            medicineobject?.aftermeal = true


        if(chkAfternoon!!.isChecked)

            medicineobject?.afternoonTime = true


        if(chkNight!!.isChecked)

            medicineobject?.nightTime = true


        val intent = Intent()
        intent.putExtra("medicineObject",medicineobject as Serializable)
        intent.putExtra("itemPosition",position)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}