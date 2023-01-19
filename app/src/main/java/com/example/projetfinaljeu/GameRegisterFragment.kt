package com.example.projetfinaljeu

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_game_register.*

class GameRegisterFragment : Fragment() {
    private lateinit var  auth: FirebaseAuth
    private val user: GameRegisterFragmentArgs by navArgs()
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_game_register, container, false)

        val passwordEditText = view.findViewById<EditText>(R.id.editTextTextPassword)

        if(user.userArgs.password!=null)
            passwordEditText.setText(user.userArgs.password)

        val emailEditText = view.findViewById<EditText>(R.id.editTextTextEmailAddress)

        if(user.userArgs.email!=null)
            emailEditText.setText(user.userArgs.email)

        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_register.setOnClickListener {
            button_register.isEnabled=false
            message_action.visibility=View.GONE
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
                button_register.isEnabled=true

            } else if (password.length < 8) {
                // password is too short
                emailEditText.error=null
                passwordEditText.error=getString(R.string.invalid_password)
                passwordEditText.background = drawable
                button_register.isEnabled=true

            }else if (password != verifypassword){
                passwordEditText.error=null
                verifyEditText.error=getString(R.string.invalid_password_confir)
                verifyEditText.background = drawable
                button_register.isEnabled=true

            }else {
                signInWithEmailAndPassword(email, password, username, view)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    private fun signInWithEmailAndPassword(email: String, password: String, username: String, view: View){

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = auth.currentUser

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()

                database.child("Users").child(username).setValue(user!!.uid)

                user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        findNavController().navigate(
                            GameRegisterFragmentDirections.actionGameRegisterFragmentToGameHomeFragment(User(email, username, user.uid,""))
                        )
                    }else{
                        message_action.visibility=View.VISIBLE
                        val messageText = view.findViewById<TextView>(R.id.message_action)
                        messageText.text=getString(R.string.message_register)

                    }
                }

            } else {
                message_action.visibility=View.VISIBLE
                val messageText = view.findViewById<TextView>(R.id.message_action)
                messageText.text=getString(R.string.message_register)
                button_register.isEnabled=true

            }
        }

    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }
}