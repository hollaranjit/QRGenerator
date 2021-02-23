package com.example.qrcodegenerator

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.qrcodegenerator.login.LoginActivity
import com.google.zxing.WriterException
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class MainActivity : AppCompatActivity() {


    lateinit var scanBtn : Button
    lateinit var qrImage : ImageView
    lateinit var saveBtn : Button
    lateinit var bitmap: Bitmap
    lateinit var data : String
    //(for later version)  val  savePath:String? = Environment.getExternalStorageDirectory().path + "/QRCode/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        qrImage= findViewById(R.id.qrPlaceHolder)
        scanBtn = findViewById(R.id.scanBtn)
        saveBtn = findViewById(R.id.saveBtn)


        var addDeviceIntent : Intent = intent

        addDeviceIntent.extras

        val bundle: Bundle? = intent.extras
        val da = bundle?.get("QRKEY")
        data = da.toString()
        val qrgEncoder: QRGEncoder = QRGEncoder(data, null, QRGContents.Type.TEXT, 700)


        try {
            bitmap = qrgEncoder.bitmap
            qrImage.setImageBitmap(bitmap)


        } catch (e: WriterException) {
            e.printStackTrace()
        }


        saveBtn.setOnClickListener(View.OnClickListener {
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                saveMediaToStorage(bitmap)
                val intent = Intent(this, EmployeeStatusActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()


            } else {
                ActivityCompat.requestPermissions(
                    this,
                    Array<String>(1) { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    0
                )
            }

        })


//        generateBtn.setOnClickListener(View.OnClickListener {
//
//            val data: String = grValue.text.toString()
//            val qrgEncoder: QRGEncoder = QRGEncoder(data, null, QRGContents.Type.TEXT, 700)
//
//
//            try {
//                bitmap = qrgEncoder.bitmap
//                qrImage.setImageBitmap(bitmap)
//
//
//            } catch (e: WriterException) {
//                e.printStackTrace()
//            }
//
//
//            saveBtn.setOnClickListener(View.OnClickListener {
//                if (ContextCompat.checkSelfPermission(
//                        getApplicationContext(),
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    ) == PackageManager.PERMISSION_GRANTED
//                ) {
//                    saveMediaToStorage(bitmap)
//
//
//                } else {
//                    ActivityCompat.requestPermissions(
//                        this,
//                        Array<String>(1) { Manifest.permission.WRITE_EXTERNAL_STORAGE },
//                        0
//                    );
//                }
//
//            })
//
//
//        })


    }
    private fun saveMediaToStorage(bitmap: Bitmap) {
        val fileName:String = data
        val filename = "${fileName}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this,"Saved to Photos",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, EmployeeStatusActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}