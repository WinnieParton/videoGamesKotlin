package com.example.projetfinaljeu

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_game_login.*


class GameLoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_game_login, container, false)

        val constraintLayout: ConstraintLayout = view.findViewById(R.id.button_connexion)
        val drawable = GradientDrawable()
        drawable.cornerRadius = 15f // set the corner radius
        drawable.setColor(ContextCompat.getColor(requireContext(),R.color.secondary))
        constraintLayout.background = drawable

       val constraintLayout1: ConstraintLayout = view.findViewById(R.id.button_creer_un_compte)
        val drawable1 = GradientDrawable()
        drawable1.cornerRadius = 15f // set the corner radius
        drawable1.setColor(ContextCompat.getColor(requireContext(),R.color.secondary))
        constraintLayout1.background = drawable1
        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emailEditText = view.findViewById<EditText>(R.id.editTextTextEmailAddress)
        emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                emailEditText.error = null
            }
        }

        //val login:Login=Login("koumwinnie@gmail.com","1234")
        button_connexion.setOnClickListener {
            val passwordEditText = view.findViewById<EditText>(R.id.editTextTextPassword)
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

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
            } else {
                auth = Firebase.auth

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        task ->
                    if(task.isSuccessful){

                        findNavController().navigate(
                            GameLoginFragmentDirections.actionGameLoginFragmentToGameHomeFragment()
                        )

                    }else{
                        Toast.makeText(activity,getString(R.string.error_create_account), Toast.LENGTH_SHORT).show()
                    }
                }


            }


        }

        button_creer_un_compte.setOnClickListener {
            findNavController().navigate(
                GameLoginFragmentDirections.actionGameLoginFragmentToGameRegisterFragment()
            )
        }

        button_mot_de_passe_oublier.setOnClickListener {
            findNavController().navigate(
                GameLoginFragmentDirections.actionGameLoginFragmentToGameForgetPasswordFragment()
            )
        }

        button_creer_un_compte.setOnClickListener { view ->
           findNavController().navigate(
               GameLoginFragmentDirections.actionGameLoginFragmentToGameRegisterFragment()
           )
        }
    }
}