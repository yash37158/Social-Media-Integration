package com.socialmediaintegration

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN = 9001
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var githubloginBtn: Button
    private lateinit var githubEdit: EditText

    private val provider = OAuthProvider.newBuilder("github.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth


        githubloginBtn = findViewById(R.id.github_Login_Btn)
        githubEdit = findViewById(R.id.githubId)
        val google_Btn = findViewById<Button>(R.id.google_login_Btn)

        FirebaseApp.initializeApp(this)

        //Google Sign In Authentication
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("841592014402-qobo7c2sg8jf7lvhid7nor9de3p45g92.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        //Github SignIn
        auth = FirebaseAuth.getInstance()

        // Target specific email with login hint.
        provider.addCustomParameter("login", githubEdit.text.toString())

        // Request read access to a user's email addresses.
        // This must be preconfigured in the app's API permissions.
        val scopes: ArrayList<String?> = object : ArrayList<String?>() {
            init {
                add("user:email")
            }
        }
        provider.scopes = scopes

        google_Btn.setOnClickListener {
            Toast.makeText(this, "Logging In with Google", Toast.LENGTH_SHORT).show()
            signIn()
        }

        githubloginBtn.setOnClickListener {
            Toast.makeText(this, "Logging In with Github", Toast.LENGTH_SHORT).show()
            signInWithGithubProvider()

        }
    }




    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
                signInIntent, RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>?) {
        try {
            val account = completedTask?.getResult(
                    ApiException::class.java
            )
            //Updating UI
            intent  = Intent(this, Dashboard::class.java)
            startActivity(intent)

            //Signed in Successfully
            val googleEmail = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleEmail)

            val googleName = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleName)

            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)

        }
        catch (e: ApiException) {
            Log.e("failed code =", e.statusCode.toString())
        }
    }

    // To check if there is a pending result, call pendingAuthResult
    private fun signInWithGithubProvider() {

        try {
            FirebaseAuthException::class.java
            auth.startActivityForSignInWithProvider(this, provider.build())
                    .addOnSuccessListener{
                        // User is signed in.
                        // retrieve the current user
                        firebaseUser = auth.currentUser!!

                        // Navigate to Dashboard
                        intent = Intent(this, Dashboard::class.java)
                        //send github User from MainActivity to Dashboard
                        intent.putExtra("githubUserEmail", firebaseUser.email)
                        this.startActivity(intent)
                        Toast.makeText(this, "Login Successfully ", Toast.LENGTH_LONG).show()
                    }
                .addOnFailureListener{
                    //Handle failure
                    Toast.makeText( this, "Error : $it", Toast.LENGTH_LONG).show()
                }
        }
        catch (e: FirebaseAuthException)
        {
            Toast.makeText(this, "Login UnSuccessfull", Toast.LENGTH_LONG).show()
        }
    }

}

