package com.example.testapp.activities

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Toast
import com.example.testapp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_otp.*

class OTPActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database : DatabaseReference
    private var verificationId: String = ""
    private var phoneNum: String = ""
    private var otp: String = ""
    private var picUrl: String = ""
    private var verified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        // receive content from previous activity through intent
        verificationId = intent.getStringExtra("verificationId")
        phoneNum = intent.getStringExtra("phoneNum")
        picUrl = intent.getStringExtra("picUrl")


        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setCancelable(false)
        mAuth = FirebaseAuth.getInstance()  // initialise auth
        database = FirebaseDatabase.getInstance().reference  // initialise database reference

        otpSendTo.text = "OTP send to " + phoneNum.replace("+91", "")

        // function to set cursor on edittext to forward on every user input
        OTPEditTextFunction()


        buttonVerify.setOnClickListener {

            if (!TextUtils.isEmpty(editText1.text.toString())  && !TextUtils.isEmpty(editText2.text.toString()) &&
                !TextUtils.isEmpty(editText3.text.toString()) && !TextUtils.isEmpty(editText4.text.toString()) &&
                !TextUtils.isEmpty(editText5.text.toString()) && !TextUtils.isEmpty(editText6.text.toString())) {

                progressDialog.setMessage("Verifying OTP...")
                progressDialog.show()

                otp = editText1.text.toString() + editText2.text.toString() + editText3.text.toString() +
                        editText4.text.toString() + editText5.text.toString() + editText6.text.toString()

                authenticate()

                // countdown timer for 30 sec
                object : CountDownTimer(30000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        if (!verified) {
                            toast("Time's Up!")
                            suspiciousUserEncountered()
                        }
                    }
                }.start()

            } else {
                toast("Enter full OTP!")
            }
        }

        arrow.setOnClickListener {
            super.onBackPressed()
        }

    }


    private fun writeVisitorToDB () {
        val visitor = MainActivity.Visitor(phoneNum, picUrl, 1)
        database.child("visitor").push().setValue(visitor)
    }

    private fun writeSuspiciousUserToDB () {
        val suspiciousUser = MainActivity.SuspiciousUser(phoneNum, picUrl)
        database.child("suspiciousUser").push().setValue(suspiciousUser)
    }

    private fun suspiciousUserEncountered () {
        writeSuspiciousUserToDB()
        startActivity(Intent(this, MainActivity::class.java)
            .putExtra("picUrl", ""))
    }


    // to authenticate the visitor
    private fun authenticate() {
        if (verificationId != null) {
            val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, otp)
            signIn(credential)
        }
    }

    private fun signIn(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                    task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    toast("Signing in...")
                    progressDialog.dismiss()
                    verified = true
                    writeVisitorToDB()
                    toast("New Visitor Saved!")
                    startActivity(Intent(this, MainActivity::class.java)
                        .putExtra("picUrl", ""))
                    finish()
                } else if (task.exception is FirebaseAuthUserCollisionException) {
                    progressDialog.dismiss()
                    toast("User already exist!")
                    suspiciousUserEncountered()

                } else {
                    progressDialog.dismiss()
                    toast("Enter correct OTP!")

                    editText1.setText("")
                    editText2.setText("")
                    editText3.setText("")
                    editText4.setText("")
                    editText5.setText("")
                    editText6.setText("")

                    suspiciousUserEncountered()
                }
            }
    }



    // function to set cursor on edittext to forward on every user input
    private fun OTPEditTextFunction () {

        editText1.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))
        editText2.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))
        editText3.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))
        editText4.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))
        editText5.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))
        editText6.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))

        editText1.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editText1.text.toString().length > 0)
                {
                    editText2.requestFocus()
                }
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }
            override fun afterTextChanged(s: Editable) {}
        })

        editText2.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editText2.text.toString().length > 0)
                {
                    editText3.requestFocus()
                }
                if (editText2.text.toString().length == 0)
                {
                    editText1.requestFocus()
                }
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }
            override fun afterTextChanged(s: Editable) {}
        })

        editText3.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editText3.text.toString().length > 0)
                {
                    editText4.requestFocus()
                }
                if (editText3.text.toString().length == 0)
                {
                    editText2.requestFocus()
                }
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }
            override fun afterTextChanged(s: Editable) {}
        })


        editText4.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editText4.text.toString().length > 0)
                {
                    editText5.requestFocus()
                }
                if (editText4.text.toString().length == 0)
                {
                    editText3.requestFocus()
                }
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }
            override fun afterTextChanged(s: Editable) {}
        })


        editText5.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editText5.text.toString().length > 0)
                {
                    editText6.requestFocus()
                }
                if (editText5.text.toString().length == 0)
                {
                    editText4.requestFocus()
                }
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }
            override fun afterTextChanged(s: Editable) {}
        })


        editText6.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editText6.text.toString().length == 0)
                {
                    editText5.requestFocus()
                }
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }
            override fun afterTextChanged(s: Editable) {}
        })

    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
