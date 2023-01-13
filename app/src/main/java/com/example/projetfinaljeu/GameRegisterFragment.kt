package com.example.projetfinaljeu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.fragment_game_register.*


class GameRegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

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


        signup_submit_btn.setOnClickListener {
           val username = "moneld"
           val email = "moneldevops+1@gmail.com"
           val password = "password"
           val password_confirm = "password"

          register(username,email,password,password_confirm)


        }
    }

    private   fun register(username: String, email: String, password: String, password_confirm: String) {

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || password_confirm.isEmpty()){
            Toast.makeText(activity,"Veuillez renseigner tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != password_confirm){
            Toast.makeText(activity,"Les deux mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
            return
        }

        auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
            if(task.isSuccessful){
                Toast.makeText(activity,"Inscription avec succès", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(activity,"Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show()
            }
        }


    }
}