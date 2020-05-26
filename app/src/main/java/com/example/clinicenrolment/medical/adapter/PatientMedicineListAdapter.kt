package com.example.clinicenrolment.medical.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicenrolment.R

import com.example.clinicenrolment.model.Medicine
import kotlinx.android.synthetic.main.row_layout_for_medicine_list.view.*


class PatientMedicineListAdapter(val dataList :ArrayList<Medicine>, val context: Context) :
    RecyclerView.Adapter<PatientMedicineListAdapter.ViewHolder>() {




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

             var  listPojo= dataList.get(position)

        if (!listPojo.medicineName?.isNullOrEmpty()) {

            holder.tvMedeicine.text = listPojo.medicineName
        }

        if(!listPojo.medicineQty?.isNullOrEmpty())
        {
            holder.tvMedicineQty.text = listPojo.medicineQty
        }

        if(listPojo.beforeMeal)
        {

            holder.chckBeforeMeal.isChecked = true
        }


        if(listPojo.aftermeal)
        {

            holder.chckAfterMeal.isChecked = true
        }



        if(listPojo.morningTime)
        {

            holder.chckMorning.isChecked = true
        }


        if(listPojo.afternoonTime)
        {

            holder.chckAfternoon?.isChecked = true
        }


        if(listPojo.nightTime)
        {

            holder.chckNight?.isChecked = true
        }



       // (holder as ViewHolder).bind(dataList[position],clickListAdapter)

    }

    override fun getItemCount(): Int {

       return dataList.size
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_layout_for_patient_medicine_list,
                parent,
                false
            )
        )
    }


   inner class ViewHolder(view : View): RecyclerView.ViewHolder(view) ,View.OnClickListener {


        override fun onClick(v: View?) {

        }


        val tvMedeicine = itemView.findViewById<TextView>(R.id.txtMedicineName)

        val  tvMedicineQty  = itemView.findViewById<TextView>(R.id.txtMedicineQty)

        val chckBeforeMeal =itemView.findViewById<CheckBox>(R.id.chkABeforeMeal)

        val chckAfterMeal =itemView.findViewById<CheckBox>(R.id.chkAfterMeal)

        val chckMorning =itemView.findViewById<CheckBox>(R.id.chkMorning)

        val chckAfternoon =itemView.findViewById<CheckBox>(R.id.chkAfternoon)

        val chckNight =itemView.findViewById<CheckBox>(R.id.chkNight)



       /* fun bind(pojo:Medicine,clickListAdapter: (Medicine) -> Unit){


          *//*  itemView.txtPatientNameValue.text = pojo.patientName
            itemView.txtPatientAgeValue.text = pojo.patientAge
            itemView.txtPatientMobileValue.text = pojo.patientMobileNo
            itemView.txtPatientReasonValue.text = pojo.reasonToMeet

            *//*

            if (!pojo.medicineName?.isNullOrEmpty()) {
                itemView.txtMedicineName.text = pojo.medicineName
            }

            if(!pojo.medicineQty?.isNullOrEmpty())
            {
                itemView.txtMedicineQty.text = pojo.medicineQty
            }

            if(pojo.beforeMeal)
            {

                itemView.chkABeforeMeal.isChecked = true
            }


            if(pojo.aftermeal)
            {

                itemView.chkAfterMeal.isChecked = true
            }



            if(pojo.morningTime)
            {

                itemView.chkMorning.isChecked = true
            }


            if(pojo.afternoonTime)
            {

                itemView.chkAfternoon?.isChecked = true
            }


            if(pojo.nightTime)
            {

                itemView.chkNight?.isChecked = true
            }


            itemView.imgEdit.setOnClickListener{clickListAdapter(pojo)}

            itemView.imgDelete.setOnClickListener{clickListAdapter(pojo)}

            itemView.setOnClickListener{clickListAdapter(pojo)}

        }
*/





    }



}