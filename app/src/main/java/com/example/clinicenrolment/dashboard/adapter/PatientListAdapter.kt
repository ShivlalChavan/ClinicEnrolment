package com.example.clinicenrolment.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicenrolment.R

import com.example.clinicenrolment.model.AddPateintModel
import com.example.clinicenrolment.model.PatientDetail
import kotlinx.android.synthetic.main.row_layout_for_patient_list.view.*

class PatientListAdapter(val dataList :ArrayList<PatientDetail>, val clickListAdapter: (PatientDetail) -> Unit, val context: Context) :
    RecyclerView.Adapter<PatientListAdapter.ViewHolder>() {




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        /*val pojo :AddPateintModel = dataList[position]

        holder?.patientName?.text = pojo.patientName

        holder?.patientAge?.text= pojo.patientAge

        holder?.patientMobile?.text = pojo.patientMobileNo

        holder?.reason?.text = pojo.reasonToMeet*/

        (holder as  ViewHolder).bind(dataList[position],clickListAdapter)

    }

    override fun getItemCount(): Int {

       return dataList.size
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_layout_for_patient_list,parent,false))
    }


    class ViewHolder(view : View): RecyclerView.ViewHolder(view){

        fun bind(pojo:PatientDetail,clickListAdapter: (PatientDetail) -> Unit){


            itemView.txtPatientNameValue.text = pojo.patientName
            itemView.txtPatientAgeValue.text = pojo.patientAge
            itemView.txtPatientMobileValue.text = pojo.patientMobileNo
            //itemView.txtPatientReasonValue.text = pojo.reasonToMeet



            itemView.setOnClickListener{clickListAdapter(pojo)}

        }
    }





}