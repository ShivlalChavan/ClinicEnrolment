package com.example.clinicenrolment.model

import java.io.Serializable

class Reason (
    var reasonId:String="",
    var pateingId:String?=null,
    var reasontoMeet:String="",
    var apptmtDate:String="",
    var isChecked:Boolean=false,
    var keyid:String=""

):Serializable