package com.example.digitalreceipts.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.digitalreceipts.R
import com.example.digitalreceipts.viewmodel.LoginScreenViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginScreenFragment : Fragment() {
    private lateinit var viewModel: LoginScreenViewModel

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 123
    private var mAuth: FirebaseAuth? = null
    private lateinit var mActivity: Activity
    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_screen_fragment, container, false)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginScreenViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity = requireActivity()
        mView = view
        mAuth = FirebaseAuth.getInstance()
        createRequest()

        view.findViewById<Button>(R.id.sign_in_button).setOnClickListener {
            signIn()
        }
    }

    override fun onStart() {
        super.onStart()
        val user = mAuth!!.currentUser
        if (user != null) {
            val navController = Navigation.findNavController(mView)
            navController.navigate(R.id.action_loginScreenFragment_to_receiptsListFragment)
            Toast.makeText(mActivity, "Welcome, ${user.displayName}!", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
                Toast.makeText(mActivity, "Welcome, ${mAuth!!.currentUser?.displayName}!", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(mActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createRequest() {

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(mActivity, gso)
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(mActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth!!.currentUser
                    val navController = Navigation.findNavController(mView)
                    navController.navigate(R.id.action_loginScreenFragment_to_receiptsListFragment)
                } else {
                    Toast.makeText(mActivity, "Sorry auth failed.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}