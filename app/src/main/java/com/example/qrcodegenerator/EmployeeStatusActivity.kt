package com.example.qrcodegenerator




import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qrcodegenerator.adapter.UserAdapter
import com.example.qrcodegenerator.login.LoginActivity
import com.example.qrcodegenerator.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class EmployeeStatusActivity : AppCompatActivity() {

    private  var userAdapter : UserAdapter? = null
    private var context: Context? = null
    private  var mUsers      : List<User>? =  null
    private val PATH:String  = "https://qrcodegenerator-7f55c-default-rtdb.firebaseio.com/"
    private var recyclerView : RecyclerView? = null
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_status)
        setSupportActionBar(findViewById(R.id.toolbar))

        setTitle("Scanned Device")
        context = this
        mAuth = FirebaseAuth.getInstance()

        recyclerView   = findViewById(R.id.recyclerView)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        mUsers = ArrayList()


        retrieveAllUsers()


    }


    private fun retrieveAllUsers() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setView(R.layout.layout_loading_dialog)
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        var fireBaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val refUsers = FirebaseDatabase.getInstance(PATH).reference.child("Users")




        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<User>).clear()


                for(snapShot in p0.children)
                {

                    val user: User? = snapShot.getValue(User::class.java)




                    if(!(user!!.getUID()).equals(fireBaseUserID))
                    {
                            if(user.getDeviceID() == "")
                            {

                            }else
                              (mUsers as ArrayList<User>).add(user)

                    }

                }

                userAdapter = UserAdapter(context!!,mUsers!!)
                recyclerView!!.adapter = userAdapter

//                    this?.let {
//                        userAdapter = UserAdapter(it, mUsers as ArrayList<User>, false)
//                        recyclerView!!.adapter = userAdapter
//                    }


                dialog.dismiss()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.menuLogout -> {
                logOut()
                true
            }
            R.id.addDevice ->{
                val intent = Intent(this,DeviceActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }


    private fun logOut()
    {
        val currentUser = mAuth.currentUser
        if(currentUser != null){
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        }
    }
}