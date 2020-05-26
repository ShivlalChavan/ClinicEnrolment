package com.example.clinicenrolment.patientdetails.adapter

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
import com.example.clinicenrolment.model.PatientDetail
import com.example.clinicenrolment.model.Reason
import kotlinx.android.synthetic.main.row_layout_for_medicine_list.view.*
import kotlinx.android.synthetic.main.row_layout_for_patient_list.view.*
import kotlinx.android.synthetic.main.row_layout_for_reason_list.view.*


class ReasonListAdapter(val dataList :ArrayList<Reason>, val clickListAdapter: (Reason) -> Unit, val context: Context) :
    RecyclerView.Adapter<ReasonListAdapter.ViewHolder>() {




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

          /*   var  listPojo= dataList.get(position)

        if (!listPojo.reasontoMeet?.isNullOrEmpty()) {

            holder.txtReasonValue.text = listPojo.reasontoMeet
        }

        if(!listPojo.apptmtDate?.isNullOrEmpty())
        {
            holder.txtDateValue.text = listPojo.apptmtDate
        }*/





       (holder as ViewHolder).bind(dataList[position],clickListAdapter)

    }

    override fun getItemCount(): Int {

       return dataList.size
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_layout_for_reason_list,
                parent,
                false
            )
        )
    }


   inner class ViewHolder(view : View): RecyclerView.ViewHolder(view) ,View.OnClickListener {


        override fun onClick(v: View?) {

        }


        val txtReasonValue = itemView.findViewById<TextView>(R.id.txtReasonValue)

        val  txtDateValue  = itemView.findViewById<TextView>(R.id.txtDateValue)


       fun bind(pojo:Reason,clickListAdapter: (Reason) -> Unit){


           itemView.txtReasonValue.text = pojo.reasontoMeet
           itemView.txtDateValue.text = pojo.apptmtDate
          //itemView.txtPatientReasonValue.text = pojo.reasonToMeet

           itemView.setOnClickListener{clickListAdapter(pojo)}

       }




    }



}