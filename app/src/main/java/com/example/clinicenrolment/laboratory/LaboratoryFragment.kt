package com.example.clinicenrolment.laboratory

import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicenrolment.Interface.FragmentBackPressed
import com.example.clinicenrolment.R
import com.example.clinicenrolment.common.AccountManager
import com.example.clinicenrolment.dashboard.DashboardActivity
import com.example.clinicenrolment.dashboard.adapter.PatientListAdapter
import com.example.clinicenrolment.laboratory.adapter.LabPatientListAdapter
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

class LaboratoryFragment : Fragment(), View.OnClickListener, FragmentBackPressed {

    //fragment View
    private var rootView: View? = null
    private val appointmentsRecyclerView: RecyclerView? = null
    private val txtEmptyTextView: TextView? = null
    private var coordinatorLayout: CoordinatorLayout? = null


   // private var showLoader: ShowLoader? = null
    private var accountManager: AccountManager? = null


    private val patientDataList:ArrayList<PatientDetail> = ArrayList()

    private val filteredPatientList: ArrayList<AddPateintModel> = ArrayList()

    private lateinit var database: DatabaseReference
    //var patientDataRecyclerView: RecyclerView

    private lateinit var linearLayoutManager: LinearLayoutManager

    private  lateinit var adapter: LabPatientListAdapter

    private var llProgressBar : LinearLayout?=null



    private var txtEmptyData:TextView?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home_layout, container, false)

        // Utility.overrideFonts(getActivity(), rootView); //Set Custom fonts

        initViews()

        //(activity as MainActivity).setTitle(R.string.home)


        setHasOptionsMenu(true)


        return rootView
    }


    private fun initViews() {

       // accountManager = AccountManager()
        coordinatorLayout = rootView!!.findViewById(R.id.coordinatorLayout) as CoordinatorLayout

        database = FirebaseDatabase.getInstance().reference

        llProgressBar = rootView?.findViewById(R.id.llProgressBar) as LinearLayout

        txtEmptyData = rootView?.findViewById(R.id.txtEmptyData)as TextView


    }






    override fun onResume() {
        super.onResume()

      //  val c = dbHelper.getFeebackdetail()

        populateList()

        //sendMessage()

    }



    private fun populateList() {


        llProgressBar!!.visibility = View.VISIBLE

        val list = database.child("PatientData")

        val patientList = object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {

                llProgressBar!!.visibility = View.GONE

                patientDataList.clear()

                val sdf = SimpleDateFormat("dd/M/yyyy", Locale.US)
                val currentDate = sdf.format(Date())
                Log.e(" C DATE is :","-"+currentDate)

                for (productSnapshot in data.child("patientData").child("patientList").children) {

                    var product = productSnapshot.getValue(PatientDetail::class.java)

                    if((product?.bloodTest==true )&& (product?.isDocumentAdded==false))  //&& product?.isPrescribed==false
                    {
                        patientDataList.add(product!!)
                    }


                }

                /*   //  data.children.mapIndexedNotNullTo(patientDataList)
                   var addPateintModel : AddPateintModel? =  data.getValue<AddPateintModel>(AddPateintModel::class.java)

                     if (addPateintModel != null) {
                         patientDataList.add(addPateintModel)
                     }*/

                //patientDataList = data
                Log.e("got ","list"+data.value)

                if(patientDataList.size!=0){

                    patientDataRecyclerView.visibility = View.VISIBLE
                    txtEmptyData?.visibility = View.GONE

                    linearLayoutManager = LinearLayoutManager(activity)
                    patientDataRecyclerView.layoutManager = linearLayoutManager

                     patientDataRecyclerView.addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))

                   //  patientDataRecyclerView.adapter = PatientListAdapter(patientDataList,activity)

                    adapter = LabPatientListAdapter(patientDataList,{  patientItem: PatientDetail-> onItemClicked(patientItem) },activity!!.applicationContext )
                    patientDataRecyclerView.adapter = adapter

                }
                else
                {
                    patientDataRecyclerView.visibility = View.GONE
                    txtEmptyData?.visibility = View.VISIBLE
                }



            }

            override fun onCancelled(dataError: DatabaseError) {

            }


        }

        list.addListenerForSingleValueEvent(patientList)

    }

    private fun onItemClicked(patientItem: PatientDetail) {

        /*val showDetailActivityIntent = Intent(activity, PateintPrescriptionActivity::class.java)

        // showDetailActivityIntent.putExtra("ItemId", partItem.id)
        showDetailActivityIntent.putExtra("patientObject",patientItem as Serializable)


        startActivity(showDetailActivityIntent)*/


        val bundle=Bundle()
        bundle.putSerializable("patientObject",patientItem)

        val labDocumentFragment = UploadDocumentFragment()
        labDocumentFragment.arguments = bundle

        var ft : FragmentTransaction  = activity!!.supportFragmentManager.beginTransaction()
                .addToBackStack("Labfragment")
            ft.replace(R.id.flContentRoot,labDocumentFragment)
        ft.commit()

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
        }

    }


    private fun sendMessage() {

        try {

            val sms = SmsManager.getDefault()
            sms.sendTextMessage("+918237983619",null,"Hello Message",null,null)

        } catch (e: Exception) {

            Log.e("Send","MessageException : "+e.printStackTrace())
        }

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


}