package com.example.clinicenrolment.model

import java.io.Serializable

class PatientPrescription  (


    val patientData: PatientBase,
    val pateintMedicine:MedicineBase,
    val reasonToMeet : ReasonBase


):Serializable
