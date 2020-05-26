package com.example.clinicenrolment.model

import org.w3c.dom.Comment
import java.io.Serializable

class PatientDetail(
    var id :String="",
    var pateintId: String = "",
    var patientName: String = "",
    var patientAge: String = "",
    var patientMobileNo: String = "",
    val reasonId: String = "",
    var apptDate: String = "",
    var isPrescribed:Boolean=false,
    var isTakenMedicine :Boolean=false,
    var comment: String="",
    var bloodTest:Boolean=false,
    var isDocumentAdded:Boolean=false,
    var labTestType:String=""
) : Serializable {


}