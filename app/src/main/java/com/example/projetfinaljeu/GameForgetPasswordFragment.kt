package com.example.projetfinaljeu

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_game_forget_password.*


class GameForgetPasswordFragment : Fragment() {
    private lateinit var  auth: FirebaseAuth
    private val user: GameForgetPasswordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_game_forget_password, container, false)
        val emailEditText = view.findViewById<EditText>(R.id.editTextUsername_mot_de_passe)
        if(user.userArgs.email!=null)
            emailEditText.setText(user.userArgs.email)
        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_success.visibility=View.GONE
        button_renvoyer_mot_de_passe.visibility=View.VISIBLE
        editTextUsername_mot_de_passe.visibility=View.VISIBLE

        val emailEditText = view.findViewById<EditText>(R.id.editTextUsername_mot_de_passe)

        emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                emailEditText.error = null
            }
        }
        button_renvoyer_mot_de_passe.setOnClickListener{
            text_success.visibility=View.GONE

            button_renvoyer_mot_de_passe.isEnabled=false
            val email = emailEditText.text.toString()
            val emailPattern = Regex(pattern = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
            val drawable = GradientDrawable()
            drawable.setStroke(2, ContextCompat.getColor(requireContext(),R.color.danger)) // 2 is the width of the border and Color.BLUE is the color
            val drawableok = GradientDrawable()
            drawableok.setStroke(2, ContextCompat.getColor(requireContext(),R.color.third)) // 2 is the width of the border and Color.BLUE is the color

            if (!email.matches(emailPattern)) {
                // email is invalid
                emailEditText.error=getString(R.string.invalid_email)
                emailEditText.background = drawable
                button_renvoyer_mot_de_passe.isEnabled=true

            } else{
                emailEditText.error=null
                emailEditText.background = drawableok

                loader.visibility=View.VISIBLE
                resetPassword(email)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }
    private fun resetPassword(email: String){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                button_renvoyer_mot_de_passe.isEnabled=true
                text_success.visibility=View.VISIBLE
                editTextUsername_mot_de_passe.text.clear()
                loader.visibility=View.GONE

            }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }
}