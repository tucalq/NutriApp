package com.arthurlunardi.nutriapp.ui.fragments.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arthurlunardi.nutriapp.AuthActivity
import com.arthurlunardi.nutriapp.NutriActivity
import com.arthurlunardi.nutriapp.R
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginFragment : Fragment(R.layout.fragment_login) {

    lateinit var auth : FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        loginBtnSubmit.setOnClickListener {
            loginUser()
        }

    }

    private fun loginUser() {
        loginBtnSubmit.showProgress {
            buttonText = "Entrando"
            progressColor = Color.WHITE
        }
        val email = loginEtEmail.text.toString().trim()
        val password = loginEtPassword.text.toString().trim()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                    }
                    loginBtnSubmit.hideProgress("Entrar")
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                        loginBtnSubmit.hideProgress("Entrar")
                    }
                }
            }
        }
    }


    private fun checkLoggedInState() {
        if (auth.currentUser == null) {
            val i = Intent(activity, AuthActivity::class.java)
            startActivity(i)
        } else {
            try {
                val ref = FirebaseDatabase.getInstance().getReference("Users")
                ref.child(auth.uid!!)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val userType = "${snapshot.child("userType").value}"
                            if (userType == "admin") {
//                                val i = Intent(activity, AdminActivity::class.java)
//                                startActivity(i)
                            } else {
                                val i = Intent(activity, NutriActivity::class.java)
                                startActivity(i)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
            } catch (e: Exception) {
                Log.e("teste", "loadUserInfo: ${e.message}")
            }

        }
    }

}