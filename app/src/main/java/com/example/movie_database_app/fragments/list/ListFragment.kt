package com.example.movie_database_app.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_database_app.R
import com.example.movie_database_app.viewmodel.MovieViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListFragment : Fragment() {

    private lateinit var mMovieViewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val adapter = MovieListAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mMovieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        mMovieViewModel.readAllData.observe(viewLifecycleOwner, Observer { movie ->
            adapter.setData(movie)
        })

        // add new movie button
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        // show delete_menu
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                val builder = AlertDialog.Builder(requireContext())
                builder.setPositiveButton("Yes") { _, _ ->
                    // delete all data in movie db table
                    mMovieViewModel.deleteAllMovies()

                    // show success message
                    Toast.makeText(requireContext(), "Movie database cleared.", Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("No") { _, _ ->
                    // do nothing
                }
                builder.setTitle("Clear Movie Database?")
                builder.setMessage("Are you sure you want to delete all movies from the database?")
                builder.create().show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}