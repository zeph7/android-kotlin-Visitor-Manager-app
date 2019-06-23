package com.example.testapp.activities

import  android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.camerakit.CameraKitView
import com.example.testapp.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_photo.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class PhotoActivity : AppCompatActivity() {

    private lateinit var cameraKitView: CameraKitView
    private lateinit var progressDialog: ProgressDialog
    private var filePath: Uri? = null
    private var photoUrl: String? = ""
    private var storage: FirebaseStorage? =null
    private var storageReference: StorageReference? = null
    private var PICK_IMAGE_REQUEST = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // to remove notification bar
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_photo)
        cameraKitView = findViewById(R.id.camera)
        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setCancelable(false)
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        // to capture image using CameraKit

        clickShot.setOnClickListener{
            cameraKitView.captureImage { cameraKitView, capturedImage ->
                val savedPhoto = File(Environment.getExternalStorageDirectory(), "photo.jpg")
                //Toast.makeText(this, "photo saved", Toast.LENGTH_SHORT).show()
                try {
                    val outputStream = FileOutputStream(savedPhoto.path)
                    outputStream.write(capturedImage)
                    outputStream.close()
                } catch (e: java.io.IOException) {
                    e.printStackTrace()
                }
            }
            Toast.makeText(this, "photo clicked", Toast.LENGTH_SHORT).show()
        }

        gallery.setOnClickListener {
            showFileChooser()
        }

        backButton.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun uploadFile() {
        if (filePath != null) {
            progressDialog.setMessage("Uploading...")
            progressDialog.show()

            var imageLink = "user_images/" + UUID.randomUUID().toString() + ".jpg"
            val imageRef = storageReference!!.child(imageLink)

            imageRef.putFile(filePath!!).addOnCompleteListener {

                imageRef.downloadUrl.addOnCompleteListener { taskSnapshot ->
                    photoUrl = taskSnapshot.result.toString()
                    progressDialog.setMessage("Uploaded...")
                    progressDialog.dismiss()
                    Toast.makeText(this, "Photo Uploaded", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@PhotoActivity, MainActivity::class.java)
                        .putExtra("picUrl", photoUrl!!))
                }

            }
                .addOnCanceledListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Upload Failed !", Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapShot ->
                    progressDialog.setMessage("Uploading...")
                }

        } else {
            Toast.makeText(this, "Add a picture !", Toast.LENGTH_SHORT).show()
        }

    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null) {

//            val thumbFile = File(data.data.path)
//            val compressedImage = Compressor(this)
//                .setMaxWidth(640)
//                .setMaxHeight(480)
//                .setQuality(75)
//                .setCompressFormat(Bitmap.CompressFormat.WEBP)
//                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                .compressToFile(thumbFile)
            filePath = data.data

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                uploadFile() // file uploaded to cloud
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    override fun onStart() {
        super.onStart()
        cameraKitView.onStart()
    }

    override fun onResume() {
        super.onResume()
        cameraKitView.onResume()
    }

    override fun onPause() {
        cameraKitView.onPause()
        super.onPause()
    }

    override fun onStop() {
        cameraKitView.onStop()
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
