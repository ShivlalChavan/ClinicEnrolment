package com.example.clinicenrolment.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicenrolment.Fragment.HomeFragment
import com.example.clinicenrolment.Interface.FragmentBackPressed
import com.example.clinicenrolment.R
import com.example.clinicenrolment.common.AccountManager
import com.example.clinicenrolment.laboratory.LaboratoryFragment
import com.example.clinicenrolment.medical.MedicalFragment
import com.example.clinicenrolment.reception.AddPatientDetailActivity
import com.example.clinicenrolment.reception.Fragment.PaientListFragment
import com.example.clinicenrolment.searchpatient.SearchPateintActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard_layout.*
import kotlinx.android.synthetic.main.toolbar_main.*

class DashboardActivity :AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{



    internal var Navigationflag = true

    lateinit var backPressedInterface: FragmentBackPressed

    //Permission Related Setup
    //private val permissionRequest: PermissionHelper.PermissionBuilder? = null

    //Drawer Related
    private var drawer: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var accountManager: AccountManager? = null
    private var coordinatorLayout: CoordinatorLayout? = null
    var toolbar: Toolbar?=null
    private val frameLayout: FrameLayout? = null

    private var flagForOptionMenu = false
    private var imgUserAvatar: ImageView? = null
    private var back_pressed: Long = 0
    private val menuItem: MenuItem? = null

    private var mAuth: FirebaseAuth? = null

   //private var user



    private val appLink: String? = null
    private val flagForUptoDate = false
    private val recyclerView: RecyclerView? = null

    internal val currentFragment: Fragment?
        get() {
            return supportFragmentManager
                .findFragmentById(R.id.flContentRoot)
        }









    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

      //  setContentView(R.layout.activity_dashboard_layout)

        val view = layoutInflater.inflate(R.layout.activity_dashboard_layout, null)

        //Utility.overrideFonts(this, view)

        setContentView(view)

        initViews()


       // setSupportActionBar(toolbar_main)

       // accountManager = AccountManager(this)




       /* val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar_main, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
*/




       /* val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
*/



    }

    private fun initViews() {

        accountManager = AccountManager(this)
        //progress wheel Initialization
        // showLoader = ShowLoader(this@MainActivity)


        toolbar = findViewById(R.id.toolbar_main) as Toolbar
        toolbar?.title = resources.getString(R.string.home)
        setSupportActionBar(toolbar)

        coordinatorLayout = findViewById(R.id.coordinatorLayout) as CoordinatorLayout
        drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        navigationView = findViewById(R.id.nav_view) as NavigationView

        val headerView = navigationView!!.getHeaderView(0)
       // txtUserName = headerView.findViewById(R.id.userNameDrawer) as TextView
        //  txtUserEmailAddress = headerView.findViewById(R.id.userEmailDrawer) as TextView
        //txtUserMobileDrawer = headerView.findViewById(R.id.userMobileDrawer) as TextView
        //imgUserAvatar = headerView.findViewById(R.id.imgUserAvatar) as ImageView


        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer!!.addDrawerListener(toggle)

        drawer!!.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {
                // Utility.hideSoftKeyboard(this@MainActivity)
            }
        })

        navigationView!!.setNavigationItemSelectedListener(this)

        toggle.syncState()

        mAuth = FirebaseAuth.getInstance()

        val user = mAuth!!.currentUser

        Log.e("USER","LOGEEDIN"+user?.email);


        val userLogin = accountManager?.userLoggenIn.toString()

        Log.e("LoggedIn","User:-"+userLogin)


        if(user!!.email.equals("doctor@gmail.com"))
        {

            nav_view.menu.findItem(R.id.nav_addPatient).setVisible(false)

            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.flContentRoot, HomeFragment())
                .addToBackStack(null)
                .commit()

        }
        else if(user!!.email.equals("reception@gmail.com"))
            {
                nav_view.menu.findItem(R.id.nav_search_patient).setVisible(false)

                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.flContentRoot, PaientListFragment())
                    .addToBackStack(null)
                    .commit()

            }
        else if(user!!.email.equals("medical@gmail.com")) // pass : medical123
        {
            nav_view.menu.findItem(R.id.nav_addPatient).setVisible(false)

            nav_view.menu.findItem(R.id.nav_search_patient).setVisible(false)

            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.flContentRoot, MedicalFragment())
                .addToBackStack(null)
                .commit()

        }
        else if(user!!.email.equals("laboratory@gmail.com")) // pass : medical123
        {
            nav_view.menu.findItem(R.id.nav_addPatient).setVisible(false)

            nav_view.menu.findItem(R.id.nav_search_patient).setVisible(false)

            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.flContentRoot, LaboratoryFragment())
                .addToBackStack(null)
                .commit()

        }



    }

    override fun onStart() {
        super.onStart()




    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        var fragment: Fragment? = null
        var tempFragment: Fragment? = null
        var flagToAddFragment = true
        item.isChecked = true


        when (item.itemId) {

            R.id.home -> {

                Navigationflag = true
                flagForOptionMenu = false

                fragment = HomeFragment()
                tempFragment = currentFragment
                if (tempFragment != null) {
                    if (tempFragment is HomeFragment) {
                        flagToAddFragment = false
                    }
                }
            }

            R.id.nav_addPatient -> {

                val intent = Intent(this, AddPatientDetailActivity::class.java)
                startActivity(intent)


            }
            R.id.nav_search_patient -> {
                val intent = Intent(this, SearchPateintActivity::class.java)
                startActivity(intent)
            }
            R.id.logout ->
            {

                 accountManager!!.logoutUserSuccessfully(this)

            }

        }
        if (fragment != null) {
            if (flagToAddFragment) {
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.flContentRoot, fragment!!)
                    .addToBackStack(null)
                    .commit()
            }

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment")
        }

        drawer!!.closeDrawer(GravityCompat.START)
        return false
    }



    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        for (fragment in supportFragmentManager.fragments) {
            handleResult(fragment, requestCode, resultCode, data)
        }

        when (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().

            REQUEST_PERMISSION_SETTING -> sentToSettings = true
        }
    }


    private fun handleResult(frag: Fragment, requestCode: Int, resultCode: Int, data: Intent?) {

        val frags = frag.childFragmentManager.fragments
        if (frags != null) {
            for (f in frags) {
                if (f != null) {
                    handleResult(f!!, requestCode, resultCode, data)
                }
            }
        }
    }

    override fun onPostResume() {
        super.onPostResume()

        Log.e("Ethe yeto Ka", "")
        if (sentToSettings) {
            sentToSettings = false
        }
    }

    override fun onBackPressed() {
        Log.e(
            "Fragment Count", "" + supportFragmentManager
                .backStackEntryCount
        )

        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            if (drawer!!.isDrawerOpen(GravityCompat.START)) {
                drawer!!.closeDrawers()
            }
        } else if (supportFragmentManager.backStackEntryCount > 1) {
            for (fragment in supportFragmentManager.fragments) {
                handleBackPressed(fragment)
            }

        } else {
            if (back_pressed + 1000 > System.currentTimeMillis()) {
                Log.e(
                    "Count Madhe ss",
                    "" + fragmentManager.backStackEntryCount
                )
                finish()

            } else {
                Toast.makeText(
                    baseContext,
                    "Press once again to exit " + resources
                        .getString(R.string.app_name) + " !",
                    Toast.LENGTH_SHORT
                ).show()
            }
            back_pressed = System.currentTimeMillis()
        }
    }

    private fun handleBackPressed(fragment: Fragment) {
        if (fragment is FragmentBackPressed) {
            backPressedInterface = fragment as FragmentBackPressed
            backPressedInterface.onBackKeyPressed()
        }

    }

    companion object {
        var permissions = java.util.ArrayList<String>()
        val requestCode = 1
        val REQUEST_PERMISSION_SETTING = 101
        var sentToSettings = false
    }





}