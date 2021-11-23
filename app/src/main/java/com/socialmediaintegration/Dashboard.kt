package com.socialmediaintegration

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Dashboard : AppCompatActivity() {


    lateinit var mGoogleSignInClient: GoogleSignInClient
    var userName = ""
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        val img = findViewById<ImageView>(R.id.profile_ImageView)
        var email = findViewById<TextView>(R.id.email_textView)
        val name = findViewById<TextView>(R.id.name_textView)
        val logoutBtn = findViewById<Button>(R.id.logout_Btn)

       // Google Sign In
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null)
        {
            val profileImg: Uri? = acct.photoUrl
            val profileName = acct.displayName
            val profileEmail = acct.email


            name.text = profileName
            email.text = profileEmail

            Glide.with(applicationContext)
                .load(profileImg.toString())
                .into(img)

        }



        logoutBtn.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val intent = Intent(this, MainActivity::class.java)
                this.startActivity(intent)
            }
        }



    }
}