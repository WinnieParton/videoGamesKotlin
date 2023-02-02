package com.example.projetfinaljeu

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_game_login.*


class GameLoginFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var  auth: FirebaseAuth
    private var user: User=User(null,null,null,null)
    private var callback: OnBackPressedCallback? = null
    private val viewModel by viewModels<SharedViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_game_login, container, false)
        sharedViewModel.getData().observe(viewLifecycleOwner, Observer { data ->
            // update the UI with the data
            // ...
            println("999999999999  "+data.size)
        })

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                val currentDestination = navController.currentDestination

                if(auth.currentUser == null || currentDestination?.id == R.id.gameHomeFragment){
                    message_action.visibility=View.VISIBLE
                    message_action.text=getString(R.string.message_login_home)
                }
                else{
                    navController.popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_mot_de_passe_oublier.applyUnderlineText(getString(R.string.partie1))

        val emailEditText = view.findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordEditText = view.findViewById<EditText>(R.id.editTextTextPassword)

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                user= User(s.toString(), "","",user.password)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                user= User(user.email, "","",s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                emailEditText.error = null
            }
        }

        button_connexion.setOnClickListener {
            button_connexion.isEnabled=false
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val emailPattern = Regex(pattern = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
            val drawable = GradientDrawable()
            drawable.setStroke(2, ContextCompat.getColor(requireContext(),R.color.danger)) // 2 is the width of the border and Color.BLUE is the color
            val drawableok = GradientDrawable()
            drawableok.setStroke(2, ContextCompat.getColor(requireContext(),R.color.third)) // 2 is the width of the border and Color.BLUE is the color

            if (!email.matches(emailPattern)) {
                // email is invalid
                emailEditText.error=getString(R.string.invalid_email)
                emailEditText.background = drawable
                button_connexion.isEnabled=true

            } else if (password.length < 8) {
                // password is too short
                emailEditText.error=null
                emailEditText.background = drawableok

                passwordEditText.error=getString(R.string.invalid_password)
                passwordEditText.background = drawable
                button_connexion.isEnabled=true

            } else {
                button_connexion.isEnabled=false
                passwordEditText.error=null
                passwordEditText.background = drawableok
                emailEditText.error=null
                emailEditText.background = drawableok
                login(email, password)
            }
        }

        button_creer_un_compte.setOnClickListener {
            findNavController().navigate(
                GameLoginFragmentDirections.actionGameLoginFragmentToGameRegisterFragment(user)
            )
        }

        button_mot_de_passe_oublier.setOnClickListener {
            findNavController().navigate(
                GameLoginFragmentDirections.actionGameLoginFragmentToGameForgetPasswordFragment(user)
            )
        }
    }


    private fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    if (user != null) {
                        findNavController().navigate(
                            GameLoginFragmentDirections.actionGameLoginFragmentToGameHomeFragment(
                                User(
                                    user.email!!,
                                    user.displayName!!,
                                    user.uid,
                                    ""
                                )
                            )
                        )
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    message_action.visibility=View.VISIBLE
                    message_action.text=getString(R.string.message_login)
                    button_connexion.isEnabled=true

                }
            }

    }
}