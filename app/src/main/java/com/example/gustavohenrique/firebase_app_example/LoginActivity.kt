package com.example.gustavohenrique.firebase_app_example

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider


const val REQUEST_GOOGLE_SIGN_IN = 9001

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        bindingButtonActions()
    }

    private fun bindingButtonActions() {

        btnLogin.setOnClickListener {
            login(txtEmailRegister.text.toString(),
                    txtPasswordRegister.text.toString())
        }

        btnRegisterUser.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }

        btnForgotPassword.setOnClickListener {
            startActivity(Intent(applicationContext,ForgotPasswordActivity::class.java))
        }

        btnLoginGoogle.setOnClickListener { loginWithGoogle() }
    }

    private fun login(email: String, password: String) {
        val mAuth = FirebaseAuth.getInstance()

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this@LoginActivity){
            if (it.isSuccessful)
                startActivity(Intent(applicationContext,MainScreenActivity::class.java))
             else
                Toast.makeText(applicationContext, "Error on login user", Toast.LENGTH_SHORT).show()

        }
    }

    private fun loginWithGoogle() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQUEST_GOOGLE_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {

        val mAuth = FirebaseAuth.getInstance()

        var credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) {

                if (it.isSuccessful)
                    startActivity(Intent(this, MainScreenActivity::class.java))
                else
                    Toast.makeText(applicationContext,"Failed to login with Firebase Credential",Toast.LENGTH_SHORT)
                            .show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == REQUEST_GOOGLE_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)

                if (account != null)
                    firebaseAuthWithGoogle(account)
                else
                    Toast.makeText(applicationContext, "Account for Sign in dont exists", Toast.LENGTH_SHORT).show()

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(applicationContext, "Sign in Failed",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

}
