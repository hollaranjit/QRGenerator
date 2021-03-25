package com.example.qrcodegenerator




import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception


class EmployeeStatusActivity : AppCompatActivity() {

    private  var userAdapter : UserAdapter? = null
    private var context: Context? = null
    private  var mUsers      : List<User>? =  null
    private var dUsers       : List<String>? = null
    private val PATH:String  = "https://qrcodegenerator-7f55c-default-rtdb.firebaseio.com/"
    private var recyclerView : RecyclerView? = null
    private lateinit var mAuth: FirebaseAuth


    //For PDF Contents
    private val TAG = "PdfCreatorActivity"
    private val REQUEST_CODE_ASK_PERMISSIONS = 111
    private var pdfFile: File? = null
    lateinit var imgdownload: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_status)
        setSupportActionBar(findViewById(R.id.toolbar))

        imgdownload = findViewById(R.id.imgPdf);
        setTitle("Scanned Device")
        context = this
        mAuth = FirebaseAuth.getInstance()

        recyclerView   = findViewById(R.id.recyclerView)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        mUsers = ArrayList()
        dUsers = ArrayList()

        retrieveAllUsers()


        imgdownload.setOnClickListener(View.OnClickListener {
            try {
                createPdfWrapper()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: DocumentException) {
                e.printStackTrace()
            }
        })

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
                            if(false)
                            {

                            }else
                            {
                              (mUsers as ArrayList<User>).add(user)

                            }

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

    ////////////////////////////////////////////////////////////////////////PDF FUNCTIONS

    @Throws(FileNotFoundException::class, DocumentException::class)
    private fun createPdfWrapper() {
        val hasWriteStoragePermission = ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            DialogInterface.OnClickListener { dialog, which ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(
                                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                            REQUEST_CODE_ASK_PERMISSIONS
                                    )
                                }
                            })
                    return
                }
                requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_CODE_ASK_PERMISSIONS
                )
            }
            return
        } else {
            createPdf()
        }
    }
    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }

    @Throws(FileNotFoundException::class, DocumentException::class)
    private fun createPdf() {

        val pdfname = "GiftItem.pdf"


        try {
            val docsFolder: File = File(Environment.getExternalStorageDirectory(), "/Documents")
            pdfFile = File(docsFolder.absolutePath, pdfname)
            if (!docsFolder.exists()) {
                docsFolder.mkdir()
                Log.i(TAG, "Created a new directory for PDF")
            }



            Log.i("Tag", pdfFile.toString())
            val output: OutputStream = FileOutputStream(pdfFile)
            val document = Document(PageSize.A4)
            val table = PdfPTable(floatArrayOf(3f, 3f, 3f, 3f))
            table.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
            table.defaultCell.fixedHeight = 50f
            table.totalWidth = PageSize.A4.width
            table.widthPercentage = 100f
            table.defaultCell.verticalAlignment = Element.ALIGN_MIDDLE
            table.addCell("Name")
            table.addCell("ID")
            table.addCell("DeviceID")
            table.addCell("Date")

            table.headerRows = 1
            val cells = table.getRow(0).cells
            for (j in cells.indices) {
                cells[j].backgroundColor = BaseColor.GRAY
            }


            var setOfDevice: String
            var setOfDates : String

            for(a in mUsers!!) {

                setOfDevice = a.getdeviceID1().keys.toString()
                setOfDates  = a.getdeviceID1().values.toString()

                table.addCell(a.getUserName())
                table.addCell(a.getZucitechID())
                table.addCell(setOfDevice)
                table.addCell(setOfDates)
            }

            //        System.out.println("Done");
            PdfWriter.getInstance(document, output)
            document.open()
            val f = Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLUE)
            val g = Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL, BaseColor.BLUE)
            document.add(Paragraph("Pdf Data \n\n", f))
            document.add(Paragraph("Pdf File Through Itext", g))
            document.add(table)
            //        for (int i = 0; i < MyList1.size(); i++) {
//            document.add(new Paragraph(String.valueOf(MyList1.get(i))));
//        }
            Toast.makeText(this,"PDF Created",Toast.LENGTH_SHORT).show()
            document.close()
        }catch (e:Exception)
        {
            e.printStackTrace()
        }



        //previewPdf()
    }

    private fun previewPdf() {
        val packageManager = context!!.packageManager
        val testIntent = Intent(Intent.ACTION_VIEW)
        testIntent.type = "application/pdf"
        val list: List<*> =
                packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list.size > 0) {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            val uri: Uri = Uri.fromFile(pdfFile)
            intent.setDataAndType(uri, "application/pdf")
            context!!.startActivity(intent)
        } else {
            Toast.makeText(
                    context,
                    "Download a PDF Viewer to see the generated PDF",
                    Toast.LENGTH_SHORT
            ).show()
        }
    }
}