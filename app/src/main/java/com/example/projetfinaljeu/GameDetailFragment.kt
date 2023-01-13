package com.example.projetfinaljeu

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class GameDetailFragment : Fragment() {
    var isItemFavorite:Boolean=true;
    var isItemWisk:Boolean=true;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_detail, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_toolbar, menu)
        menu.removeItem(R.id.close);
        menu.removeItem(R.id.logout);

        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.like -> {
                if (isItemFavorite) {
                    item.setIcon(R.drawable.like_full)
                    isItemFavorite = false
                } else {
                    item.setIcon(R.drawable.like)
                    isItemFavorite = true
                }
                return true
            }
            R.id.wish -> {
                if (isItemWisk) {
                    item.setIcon(R.drawable.whishlist_full)
                    isItemWisk = false
                } else {
                    item.setIcon(R.drawable.whishlist)
                    isItemWisk = true
                }

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}