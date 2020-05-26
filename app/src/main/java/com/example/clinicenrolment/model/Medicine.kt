package com.example.clinicenrolment.model

import java.io.Serializable


class Medicine: Serializable {

    var patientId:String?=null

    var reasodId:String?=null

    var medicineId:String?=null

    var medicineDate:String=""

    var medicineName:String =""

    var medicineQty:String = ""

    var morningTime :Boolean = false

    var afternoonTime :Boolean = false


    var nightTime :Boolean = false

    var beforeMeal :Boolean = false

    var aftermeal : Boolean = false




}





