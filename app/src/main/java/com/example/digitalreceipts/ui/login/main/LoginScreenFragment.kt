package com.example.digitalreceipts.ui.login.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.digitalreceipts.R
import com.example.digitalreceipts.api.model.LoginBody
import com.example.digitalreceipts.api.model.LoginResponse
import com.example.digitalreceipts.api.model.ResetPassword
import com.example.digitalreceipts.databinding.CreateNewAccountFragmentBinding
import com.example.digitalreceipts.databinding.LoginScreenFragmentBinding
import com.example.digitalreceipts.ui.custom.dialog.ProgressHUD
import com.example.digitalreceipts.ui.login.newaccount.CreateNewAccountViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginScreenFragment : Fragment() {
    private val mViewModel: LoginScreenViewModel by viewModel()

    private val binding: LoginScreenFragmentBinding by lazy {
        LoginScreenFragmentBinding.inflate(layoutInflater)
    }

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 123
    private var mAuth: FirebaseAuth? = null
    private lateinit var mActivity: Activity
    private lateinit var mView: View
    private lateinit var mProgressHUD: ProgressHUD

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel.apiResponse.observe(viewLifecycleOwner) {
            mProgressHUD.dismiss()

            if (it.code() == 200) {
                navigateToHomeScreenFragment(it.body()!!)
            } else {
                mProgressHUD = ProgressHUD.show(
                    context,
                    "Problem accessing account.\n\nPlease review your e-mail and password and try again.",
                    indeterminate = false,
                    cancelable = true,
                    spinnerGone = true
                )

                mProgressHUD.show()
            }
        }

        binding.btLogin.setOnClickListener {
            val loginBody = LoginBody(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )

            mProgressHUD = ProgressHUD.show(
                context, "Accessing",
                indeterminate = false,
                cancelable = false,
                spinnerGone = false
            )

            mProgressHUD.show()

            mViewModel.login(loginBody)
        }

        binding.btCreateNewAccount.setOnClickListener {
            val direction = LoginScreenFragmentDirections.navigateToCreateNewAccount()
            findNavController().navigate(direction)
        }

        binding.acbSignInWithGoogle.setOnClickListener {
            signIn()
        }

        binding.tvForgotPassword.setOnClickListener {
            val direction = LoginScreenFragmentDirections.navigateToForgotPassword()
            findNavController().navigate(direction)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity = requireActivity()
        mView = view
        mAuth = FirebaseAuth.getInstance()
        createRequest()
    }

    /*override fun onStart() {
        super.onStart()
        val user = mAuth!!.currentUser
        if (user != null) {
            val navController = Navigation.findNavController(mView)
            navController.navigate(R.id.navigate_to_receipts_list_directly)
            Toast.makeText(mActivity, "Welcome, ${user.displayName}!", Toast.LENGTH_SHORT).show()
        }
    }*/

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
                Toast.makeText(
                    mActivity,
                    "Welcome, ${mAuth!!.currentUser?.displayName}!",
                    Toast.LENGTH_SHORT
                )
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
                    navController.navigate(R.id.navigate_to_receipts_list_directly)
                } else {
                    Toast.makeText(mActivity, "Sorry auth failed.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    private fun navigateToHomeScreenFragment(userData: LoginResponse) {
        val direction = LoginScreenFragmentDirections.navigateToHomeScreen(userData)
        findNavController().navigate(direction)
    }
}