package com.example.projetfinaljeu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController


class LoaderFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_loader, container, false)
        sharedViewModel.getData().observe(viewLifecycleOwner, Observer { data ->
            findNavController().navigate(
                LoaderFragmentDirections.actionGameLoaderFragmentToGameLoginFragment()
            )
        })
        return view
    }

}