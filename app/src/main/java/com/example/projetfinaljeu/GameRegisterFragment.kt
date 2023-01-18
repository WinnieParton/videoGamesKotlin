package com.example.projetfinaljeu

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_game_register.*

class GameRegisterFragment : Fragment() {
    private lateinit var  auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_register.setOnClickListener {
            val passwordEditText = view.findViewById<EditText>(R.id.editTextTextPassword)
            val usernameEditText = view.findViewById<EditText>(R.id.editTextUsername)
            val emailEditText = view.findViewById<EditText>(R.id.editTextTextEmailAddress)
            val verifyEditText = view.findViewById<EditText>(R.id.editTextVerificationPassword)

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val username = usernameEditText.text.toString()
            val verifypassword = verifyEditText.text.toString()

            val emailPattern = Regex(pattern = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
            val drawable = GradientDrawable()
            drawable.setStroke(2, ContextCompat.getColor(requireContext(),R.color.danger)) // 2 is the width of the border and Color.BLUE is the color

            if (!email.matches(emailPattern)) {
                // email is invalid
                emailEditText.error=getString(R.string.invalid_email)
                emailEditText.background = drawable
            } else if (password.length < 8) {
                // password is too short
                emailEditText.error=null
                passwordEditText.error=getString(R.string.invalid_password)
                passwordEditText.background = drawable
            }else if (password != verifypassword){
                passwordEditText.error=null
                verifyEditText.error=getString(R.string.invalid_password_confir)
                verifyEditText.background = drawable
            }else {
                signInWithEmailAndPassword(email, password, username)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    private fun signInWithEmailAndPassword(email: String, password: String, username: String){

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = auth.currentUser
                val data = HashMap<String, Any>()
                data["username"] = username
                FirebaseFirestore.getInstance().collection("users").document(user!!.uid).set(data)
            println("************** ${auth.currentUser}")
                findNavController().navigate(
                    GameRegisterFragmentDirections.actionGameRegisterFragmentToGameHomeFragment()
                )
            } else {
                // If sign in fails, display a message to the user.
                Log.w("FirebaseAuth", "createUserWithEmail:failure", task.exception)
                Toast.makeText(context, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
                // Update UI
            }
        }

    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }
}