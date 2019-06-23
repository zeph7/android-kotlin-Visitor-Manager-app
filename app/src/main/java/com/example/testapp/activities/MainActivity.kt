package com.example.testapp.activities

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.testapp.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var database: DatabaseReference
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var verificationId = ""
    private var phoneNum: String = ""
    private var picUrl: String = ""

    data class Visitor (
        var phoneNumber: String? = "",
        var picUrl: String? = "",
        var visit_count: Int? = 1
    )

    data class SuspiciousUser (
        var phoneNumber: String? = "",
        var picUrl: String? = ""
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setCancelable(false)
        userPhoneNumber.setText("+91 ")
        userPhoneNumber.setSelection(4)
        database = FirebaseDatabase.getInstance().reference  // initialise database reference

        picUrl = intent.getStringExtra("picUrl")


        // button to register the visitor
        buttonRegister.setOnClickListener {
            phoneNum = userPhoneNumber.text.toString()
            phoneNum = phoneNum.replace("\\s".toRegex(), "")
            // if phone number not in database signup

            if (phoneNum.length != 13) {
                toast("Enter a valid phone number!")
            } else if (picUrl == "") {
                toast("Select a profile pic first!")
            } else {
                progressDialog.setMessage("Signing in...")
                progressDialog.show()

                val visitorDBref = database.child("visitor")
                var visitorList = mutableListOf<Visitor>()

                // for verification of new / existing user
                val valueEventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var flag = true
                        for (ds in dataSnapshot.children) {
                            val visitor = ds.getValue<Visitor>(Visitor::class.java)
                            visitorList.add(visitor!!)

                            if (visitor.phoneNumber!! == phoneNum) {
                                flag = false
                                val v = Visitor(visitor.phoneNumber, visitor.picUrl, visitor.visit_count!! + 1)
                                database.child("visitor").child(ds.key!!).setValue(v)

                                progressDialog.dismiss()
                                val count = visitor.visit_count!! + 1
                                toast("Welcome back for $count time")
                            }
                        }
                        if (flag) {
                            verifyPhoneNumber(phoneNum)
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d("tag", databaseError.message)
                    }
                }
                visitorDBref.addListenerForSingleValueEvent(valueEventListener)

            }
        }

        cardProfilePic.setOnClickListener {
            finish()
            startActivity(Intent(this@MainActivity, PhotoActivity::class.java))
        }
    }

    private fun verifyPhoneNumber(phoneNum: String) {
        // callbacks for phone auth
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential?) {
                toast("Verifying...")
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                //To change body of created functions use File | Settings | File Templates.
                progressDialog.dismiss()
                toast("Verification failed!")
            }

            override fun onCodeSent(verification: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(verification, p1)

                verificationId = verification.toString() // gives verification ID
                toast("Verification code sent to your number")

                finish()
                startActivity(
                    Intent(this@MainActivity, OTPActivity::class.java)
                        .putExtra("verificationId", verificationId)
                        .putExtra("phoneNum", phoneNum)
                        .putExtra("picUrl", picUrl))
            }
        }

        // Send a verification code to the user's phone
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNum,
            30,
            TimeUnit.SECONDS,
            this,
            mCallbacks
        )
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

}
