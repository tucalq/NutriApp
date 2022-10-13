package com.arthurlunardi.nutriapp.ui.fragments.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arthurlunardi.nutriapp.NutriActivity
import com.arthurlunardi.nutriapp.R
import com.arthurlunardi.nutriapp.model.User
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment(R.layout.fragment_register) {

    var email = ""
    var password = ""
    var name = ""
    lateinit var auth : FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        registerBtnSubmit.setOnClickListener {
            registerUser()
        }


    }


    private fun registerUser() {
        registerBtnSubmit.showProgress {
            buttonText = "Cadastrando"
            progressColor = Color.WHITE
        }
        name = registerName.text.toString().trim()
        email = registerEtEmail.text.toString().trim()
        password = registerEtPassword.text.toString().trim()
        val timestamp = System.currentTimeMillis()
        val uid = auth.uid
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
//                        checkLoggedInState()
                    }
                    val user = User(email, password, timestamp.toString(), uid.toString())
                    saveUser(user)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun saveUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        val timestamp = System.currentTimeMillis()
        val uid = auth.uid
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["uid"] = uid!!
        hashMap["email"] = email
        hashMap["name"] = name
        hashMap["profileImage"] = "" // add empty, will do in profile edit
        hashMap["id"] = timestamp
        hashMap["theme"] = ""

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid)
            .setValue(hashMap)
            .addOnSuccessListener {
                registerBtnSubmit.hideProgress("Cadastrar")
                val intent = Intent(activity, NutriActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    activity,
                    "Erro ao criar uma conta, ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}