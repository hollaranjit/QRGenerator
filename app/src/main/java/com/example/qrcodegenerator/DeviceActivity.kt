package com.example.qrcodegenerator

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qrcodegenerator.model.User
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

class DeviceActivity : AppCompatActivity() {
    lateinit var etDeviceID      : TextInputEditText
    lateinit var etAssetType     : TextInputEditText
    lateinit var etModelName    : TextInputEditText
    lateinit var etOSVersion      : TextInputEditText
    lateinit var btnGenerate :Button
    //FireBase variables
    var deviceName:String = ""
    var checkDevice: List<String>? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private lateinit var deviceUsers: DatabaseReference
    private val PATH:String = "https://qrcodegenerator-7f55c-default-rtdb.firebaseio.com/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        mAuth = FirebaseAuth.getInstance()

        checkDevice = ArrayList()

        etDeviceID = findViewById(R.id.etDeviceId)
        etAssetType = findViewById(R.id.etAssetType)
        etModelName = findViewById(R.id.etModelName)
        etOSVersion = findViewById(R.id.etOsVersion)
        btnGenerate = findViewById(R.id.btnGenerateQR)


        findViewById<Button>(R.id.btnAddDevice).setOnClickListener(View.OnClickListener {
            addDevice()
        })

        findViewById<Button>(R.id.btnGenerateQR).setOnClickListener(View.OnClickListener {

            if (etDeviceID.text.toString() != "") {
                val i = Intent(this, MainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                i.putExtra("QRKEY", etDeviceID.text.toString().trim())
                startActivity(i)
                finish()
            }
            else
            {
                Toast.makeText(this, "Device ID should not be empty", Toast.LENGTH_SHORT).show()

            }

        })



    }


    private fun addDevice()
    {
        val deviceID : String = etDeviceID.text.toString().trim()
        val assetType :String = etAssetType.text.toString().trim()
        val modelName : String = etModelName.text.toString().trim()
        val osVersion :String = etOSVersion.text.toString().trim()
        var boolCheck :Boolean = true

        if (deviceID.equals(""))
        {
            etDeviceID.setError("Enter Device ID")
        }
        else if (assetType.equals(""))
        {
            etAssetType.setError("Enter Asset Type")
        } else if(modelName.equals(""))
        {
            etModelName.setError("Enter Model Name")
        }else if (osVersion.equals(""))
        {
            etAssetType.setError("Enter OS Version")
        } else if(modelName.equals(""))
        {
            etModelName.setError("Enter OS Version")
        }else
        {



            refUsers = FirebaseDatabase.getInstance(PATH).reference.child("Device").child(deviceID)

            val deviceHashMap = HashMap<String, Any>()
            deviceHashMap["deviceID"] = deviceID
            deviceHashMap["assetType"] = assetType
            deviceHashMap["modelName"] = modelName
            deviceHashMap["osVersion"] = osVersion



            for(a in checkDevice!!)
            {
                if( deviceID.toLowerCase() == a.toLowerCase())
                {
                    boolCheck = false
                    break
                }

            }

            if(boolCheck != true)
            {
                Toast.makeText(applicationContext,"Device Exists", Toast.LENGTH_SHORT).show()
            }


            if (boolCheck)
           {
            refUsers.updateChildren(deviceHashMap).addOnCompleteListener { task ->
                if(task.isSuccessful)
                {
                    btnGenerate.setVisibility(View.VISIBLE)
                    Toast.makeText(this, "Updated Succesfulley", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(
                            this,
                            "Error Message" + task.exception!!.message,
                            Toast.LENGTH_SHORT
                    ).show()

                }

            }
        }

        }

    }



    override fun onStart() {
        super.onStart()
        deviceCheck()
    }

    private fun deviceCheck()
    {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setView(R.layout.layout_loading_dialog)
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        deviceUsers = FirebaseDatabase.getInstance(PATH).reference.child("Device")

        try {
            deviceUsers.addValueEventListener(object:ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {

                    for(snapShot in p0.children)
                    {

                        (checkDevice as ArrayList<String>).add(snapShot.child("deviceID").getValue().toString())


                    }
                    dialog.dismiss()
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })
        }catch (e:Exception){e.printStackTrace()}





    }

}