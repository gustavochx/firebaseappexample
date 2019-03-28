package com.example.gustavohenrique.firebase_app_example

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    var timer = object: CountDownTimer(500,1000) {
        override fun onFinish() {
            startActivity(Intent(applicationContext,MainScreenActivity::class.java))
        }
        override fun onTick(millisUntilFinished: Long) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        bindingButtonActions()
    }

    private fun bindingButtonActions() {
        btnRegisterNewUser.setOnClickListener {
            registerUser(txtEmailRegister.text.toString(),
                    txtPasswordRegister.text.toString())

        }
    }

    private fun registerUser(email: String, password: String) {
        val mAuth = FirebaseAuth.getInstance()

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(applicationContext, "Register succesful!",
                        Toast.LENGTH_SHORT).show()
                timer.start()
            } else {
                Toast.makeText(
                        applicationContext, "Cant register user",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
