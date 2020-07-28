package com.example.uploadtogoogledrive.ui.googledrive.signin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uploadtogoogledrive.R
import com.example.uploadtogoogledrive.ui.googledrive.upload.UploadActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class GoogleSignInActivity : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient

     var account: GoogleSignInAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        account = GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            Toast.makeText(this, "You Need To Sign In First", Toast.LENGTH_SHORT).show()
        }
        if (account != null) {
            val intent = Intent(this, UploadActivity::class.java)
            intent.putExtra(ACCOUNT, account)
            startActivity(intent)
        }

        btSignIn.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(
                signInIntent,
                RC_SIGN_IN
            )
        }
        btSignOut.setOnClickListener {
            signOut()
        }
    }

    fun signOut(){
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                Toast.makeText(
                    applicationContext,
                    "Signed Out", Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            account = completedTask.getResult(ApiException::class.java)
            val intent = Intent(this, UploadActivity::class.java)
            intent.putExtra(ACCOUNT, account)
            startActivity(intent)
            Timber.e("Successful Authentication${account?.email}")
        } catch (e: ApiException) {
            Timber.e("Throwable${e.statusCode}")
        }
    }

    companion object {
        private const val RC_SIGN_IN = 1
        const val ACCOUNT="account"
    }
}