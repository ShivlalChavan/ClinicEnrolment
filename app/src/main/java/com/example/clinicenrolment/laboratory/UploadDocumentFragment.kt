package com.example.clinicenrolment.laboratory

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicenrolment.Interface.FragmentBackPressed
import com.example.clinicenrolment.R
import com.example.clinicenrolment.common.AccountManager
import com.example.clinicenrolment.dashboard.DashboardActivity
import com.example.clinicenrolment.dashboard.adapter.PatientListAdapter
import com.example.clinicenrolment.model.AddPateintModel
import com.example.clinicenrolment.model.PatientDetail
import com.example.clinicenrolment.patientdetails.PateintPrescriptionActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home_layout.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

import java.io.File

import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.provider.MediaStore
import android.widget.*
import com.example.clinicenrolment.common.Connectivity
import com.example.clinicenrolment.common.Utility
import com.example.clinicenrolment.model.DocumentModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson


class UploadDocumentFragment : Fragment(), View.OnClickListener, FragmentBackPressed {

    //fragment View
    private var rootView: View? = null
    private val appointmentsRecyclerView: RecyclerView? = null
    private val txtEmptyTextView: TextView? = null
    private var coordinatorLayout: CoordinatorLayout? = null


    // private var showLoader: ShowLoader? = null
    private var accountManager: AccountManager? = null


    private lateinit var database: DatabaseReference
    //var patientDataRecyclerView: RecyclerView


    private var llProgressBar: LinearLayout? = null


    private var txtEmptyData: TextView? = null

    val GLOBALRESULTCODECAMERA = 100
    val GLOBALRESULTCODEGALLERY = 200
    private var globalFile: File? = null
    private var imgDecodableString: String? = null

    val requestCode = 1
    val REQUEST_PERMISSION_SETTING = 101
    var sentToSettings = false
    private val globalErroMessage = ""

    private var edtLabComment:EditText?=null
    private var imagePicked:ImageView?=null

    private var btnSelect:Button?=null

    private var fileUri: Uri?=null

    private var storageReference: StorageReference? = null

    private var patientObject : PatientDetail? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_lab_upload_document, container, false)

        // Utility.overrideFonts(getActivity(), rootView); //Set Custom fonts

        var bundle :Bundle = arguments!!


        patientObject = bundle.getSerializable("patientObject") as PatientDetail?

        var gson = Gson()
        Log.e("got","PatientObject"+gson.toJson(patientObject))



        initViews()

        //(activity as MainActivity).setTitle(R.string.home)


        setHasOptionsMenu(true)


        return rootView
    }


    private fun initViews() {

        // accountManager = AccountManager()
        coordinatorLayout = rootView!!.findViewById(R.id.coordinatorLayout) as CoordinatorLayout

        database = FirebaseDatabase.getInstance().reference

        storageReference = FirebaseStorage.getInstance().reference

        llProgressBar = rootView?.findViewById(R.id.llProgressBar) as LinearLayout

        imagePicked = rootView?.findViewById(R.id.imagePicked) as ImageView

        edtLabComment = rootView!!.findViewById(R.id.edtLabComment) as EditText

        btnSelect = rootView?.findViewById(R.id.btnSelect) as Button

        btnSelect!!.setOnClickListener(this)

    }


    override fun onResume() {
        super.onResume()




    }


    override fun onClick(v: View) {

        when (v.id) {


            /* R.id.btnSubmit -> {

                 userId = Utility.getStringFromEditText(edtUserId)

                 if (!Utility.checkStringNullOrEmpty(userId)) {

                     val lifestyleBundle = Bundle()
                     lifestyleBundle.putString("userId", userId)
                     val lifeStyleFragment = FeedbackFragment()

                     lifeStyleFragment.setArguments(lifestyleBundle)
                     activity!!.supportFragmentManager.beginTransaction()
                         .replace(R.id.flContentRoot, lifeStyleFragment)
                         .addToBackStack(null)
                         .commit()


                     edtUserId!!.setText("")
                     userId = ""


                 } else {
                     Utility.showSnackBar("Please enter your Id.", coordinatorLayout)
                 }
             }*/

            R.id.btnSelect ->
            {

                if (fileUri==null)
                {
                    ImagePicker.with(activity!!).crop(1f, 1f)               //Crop Square image(Optional)
                        .compress(1024)         //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                        .start { resultCode, data ->
                            if (resultCode == Activity.RESULT_OK) {
                                //Image Uri will not be null for RESULT_OK
                                 fileUri = data?.data
                                imagePicked!!.setImageURI(fileUri)

                                btnSelect!!.setText("Submit")

                                //You can get File object from intent
                                val file:File? = ImagePicker.getFile(data)

                                //You can also get File Path from intent
                                val filePath:String? = ImagePicker.getFilePath(data)

                            }
                            else if (resultCode == ImagePicker.RESULT_ERROR) {

                                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                            }
                            else {

                                Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                else if(fileUri!=null)
                {


                    if (Connectivity.isConnected(activity!!))
                    {

                        llProgressBar!!.visibility = View.VISIBLE

                        uploadImage(fileUri!!)




                    }
                    else
                    {
                        val snackbar = Snackbar.make(rootView!!.findViewById(R.id.coordinatorLayout), getString(R.string.internet_connectivity),
                            Snackbar.LENGTH_LONG)
                        snackbar.show()
                    }


                }
            }

        }

    }

    private fun uploadImage(fileUri: Uri) {


        if(fileUri != null){

            val ref = storageReference?.child("Clinic/LabDocument/" + UUID.randomUUID().toString())

            val uploadTask = ref?.putFile(fileUri!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {

                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val downloadUri = task.result

                    addDocumentListData(downloadUri.toString())

                } else
                {
                    llProgressBar!!.visibility = View.GONE

                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(activity, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }


    }

    private fun addDocumentListData(filepath: String) {


        val sdf = SimpleDateFormat("dd/M/yyyy", Locale.US)
        val currentDate = sdf.format(Date())
        Log.e(" C DATE is :","-"+currentDate)



        var comment :String  =  edtLabComment!!.text.toString()

        val dmntId = database.child("PatientData").child("patientDocument").child("documentList").push().key

        var patientLabData = DocumentModel(patientObject!!.pateintId.toString(),dmntId.toString(),comment,filepath,currentDate)

        patientObject!!.isDocumentAdded = true

        database.child("PatientData").child("patientData").child("patientList").child(patientObject!!.id).setValue(patientObject)

        database.child("PatientData").child("patientDocument").child("documentList").push().setValue(patientLabData)


        llProgressBar!!.visibility = View.GONE

         edtLabComment!!.setText("")

          activity!!.supportFragmentManager.popBackStackImmediate()


    }

    override fun onBackKeyPressed() {


        if (activity != null) {
            Log.e(
                "Fragment Count", "" + activity!!.supportFragmentManager
                    .backStackEntryCount
            )

            activity!!.supportFragmentManager.popBackStackImmediate()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {

            if (resultCode == RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data

                imagePicked!!.setImageURI(fileUri)

                //You can get File object from intent
                val file:File? = ImagePicker.getFile(data)

                //You can also get File Path from intent
                val filePath:String? = ImagePicker.getFilePath(data)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            // Utility.showSnackBar(getString(R.string.error_in_image_processing), coordinatorLayout)
            Log.e("Error in ", "" + e)
        }


    }


//        var DOWNLOADED_FILESPATH_GALLERY = activity!!.getExternalFilesDir("ClinicApp/Gallery")!!.absoluteFile




}