package com.example.projetfinaljeu

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.fragment_game_register.*


class GameRegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_register, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       button_register.setOnClickListener {
           val username_text = view.findViewById<EditText>(R.id.editTextUsername)
           val email_text = view.findViewById<EditText>(R.id.editTextTextEmailAddress)
           val password_text = view.findViewById<EditText>(R.id.editTextTextPassword)
           val password_confirm_text = view.findViewById<EditText>(R.id.editTextVerificationPassword)


          register(username_text,email_text,password_text,password_confirm_text)


        }
    }

    private   fun register(username_text: EditText, email_text: EditText, password_text: EditText, password_confirm_text: EditText) {

        val username = username_text.text.toString()
        val email = email_text.text.toString()
        val password = password_text.text.toString()
        val password_confirm = password_confirm_text.text.toString()

        val emailPattern = Regex(pattern = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        val drawable = GradientDrawable()
        drawable.setStroke(2, ContextCompat.getColor(requireContext(),R.color.danger))

        var errors_text = false;

        if (username.isEmpty()){
            errors_text = true
            username_text.error=getString(R.string.invalid_username)
            username_text.background = drawable
        }else if (email.isEmpty() || !email.matches(emailPattern)){
            errors_text = true
            username_text.error=null
            email_text.error=getString(R.string.invalid_email)
            email_text.background = drawable
        }else if (password.isEmpty() || password.length < 8 || password != password_confirm){
            errors_text = true
            email_text.error=null
            email_text.error=getString(R.string.invalid_password)
            email_text.background = drawable
        }else{
            errors_text = false
        }

      if (!errors_text){
          auth = Firebase.auth

          auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                  task ->
              if(task.isSuccessful){
                  val uid = FirebaseAuth.getInstance().uid?:""
                  databaseReference = FirebaseDatabase.getInstance("https://videogameskotlin-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")

                  val user = User(username)

                  if(uid != null){
                      databaseReference.child(uid).setValue(user).addOnCompleteListener {
                          if (it.isSuccessful){
                              Toast.makeText(activity,getString(R.string.success_create_account), Toast.LENGTH_SHORT).show()
                             findNavController().navigate(GameRegisterFragmentDirections.actionGameRegisterFragmentToGameLoginFragment())
                          }else{
                              Toast.makeText(activity,getString(R.string.error_create_account), Toast.LENGTH_SHORT).show()
                          }
                      }
                  }



              }else{
                  Toast.makeText(activity,getString(R.string.error_create_account), Toast.LENGTH_SHORT).show()
              }
          }
      }


    }
}

