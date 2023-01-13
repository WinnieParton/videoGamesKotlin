package com.example.projetfinaljeu

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_game_login.*


class GameLoginFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_login, container, false)


    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val login:Login=Login("koumwinnie@gmail.com","1234")
        button_connexion.setOnClickListener { view ->
            findNavController().navigate(
                GameLoginFragmentDirections.actionGameLoginFragmentToGameHomeFragment()
            )
        }

        button_creer_un_compte.setOnClickListener { view ->
           findNavController().navigate(
               GameLoginFragmentDirections.actionGameLoginFragmentToGameRegisterFragment()
           )
        }
    }
}