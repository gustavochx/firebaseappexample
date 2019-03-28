package com.example.gustavohenrique.firebase_app_example

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_login.*

class ForgotPasswordActivity : AppCompatActivity() {

    var timer = object: CountDownTimer(500,1000) {
        override fun onFinish() {
            startActivity(Intent(applicationContext,MainScreenActivity::class.java))
        }
        override fun onTick(millisUntilFinished: Long) {}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        bindingActions()
    }

    private fun bindingActions() {
        btnResetPassword.setOnClickListener {
            sendRecoverPassword(txtForgotEmail.text.toString())
        }
    }

    private fun sendRecoverPassword(email: String) {
        val mAuth = FirebaseAuth.getInstance()


        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(applicationContext, "E-mail enviado com sucesso!",
                        Toast.LENGTH_SHORT).show()
                timer.start()
            } else {
                Toast.makeText(applicationContext, "Não foi possível resetar a senha",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }
}
