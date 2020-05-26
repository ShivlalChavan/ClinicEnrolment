package com.example.clinicenrolment.model

import java.io.Serializable

class AddPateintModel (

   /* val patientName:String="",
    val patientAge:String="",
    val patientMobileNo:String="",
    val reasonToMeet:String="",
    val apptDate:String="",
    var pateintId:String="",
   // val prescrition:String=""
    var isprescribed :Boolean=false,

  var prescrition: ArrayList<Medicine> ?=null*/

    val patientData: PatientBase,
    val patientMedicine:MedicineBase,
    val reasonToMeet : ReasonBase,
    val patientDocument: DocumentBase


) :Serializable