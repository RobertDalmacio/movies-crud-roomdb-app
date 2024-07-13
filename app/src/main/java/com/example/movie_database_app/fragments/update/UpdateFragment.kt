package com.example.movie_database_app.fragments.update

import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movie_database_app.R
import com.example.movie_database_app.model.Movie
import com.example.movie_database_app.viewmodel.MovieViewModel

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mMovieViewModel: MovieViewModel
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { view?.findViewById<ImageView>(R.id.ivMoviePoster_update)?.setImageURI(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update, container, false)
        mMovieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        view.findViewById<EditText>(R.id.etMovieTitle_update).setText(args.currentMovie.movieTitle)
        view.findViewById<EditText>(R.id.etStudioName_update).setText(args.currentMovie.studioName)
        view.findViewById<EditText>(R.id.etCriticsRating_update).setText(args.currentMovie.criticsRating.toString())
        view.findViewById<ImageView>(R.id.ivMoviePoster_update).setImageBitmap(resizeBitmap(args.currentMovie.moviePoster, 500, 500))

        // update submit button
        view.findViewById<Button>(R.id.btnSubmit_update).setOnClickListener {
            val movieTitle = view.findViewById<EditText>(R.id.etMovieTitle_update).text.toString()
            val studioName = view.findViewById<EditText>(R.id.etStudioName_update).text.toString()
            val moviePoster = resizeBitmap(view.findViewById<ImageView>(R.id.ivMoviePoster_update).drawable.toBitmap(), 80, 100 )
            // set a default value of 0
            var criticsRating = 0
            // only convert to number id string is not empty
            if (view.findViewById<EditText>(R.id.etCriticsRating_update).text.isNotEmpty()) {
                criticsRating = view.findViewById<EditText>(R.id.etCriticsRating_update).text.toString().toDouble().toInt()
            }

            // checks if 3 required text fields are filled out
            if (inputCheck(movieTitle, studioName, view.findViewById<EditText>(R.id.etCriticsRating_update).text)) {
                // checks that critics rating is between 0 to 100
                if (criticsRating in 0..100) {
                    // create updated movie
                    val updatedMovie = Movie( args.currentMovie.id, movieTitle, studioName, criticsRating, moviePoster)

                    // updates existing entry from database
                    mMovieViewModel.updateMovie(updatedMovie)

                    // show success message
                    Toast.makeText(requireContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show()

                    // navigate back to list fragment
                    view.findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                } else {
                    Toast.makeText(requireContext(), "Please ensure rating is between 0 and 100.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        // select image button
        view.findViewById<Button>(R.id.btnSelectPoster_update).setOnClickListener {
            resultLauncher.launch("image/*")
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
                    // delete selected movie
                    mMovieViewModel.deleteMovie(args.currentMovie)

                    // show success message
                    Toast.makeText(requireContext(), "Successfully removed: ${args.currentMovie.movieTitle}", Toast.LENGTH_SHORT).show()

                    // navigate back to list fragment
                    view?.findNavController()?.navigate(R.id.action_updateFragment_to_listFragment)
                }
                builder.setNegativeButton("No") { _, _ ->
                    // do nothing
                }
                builder.setTitle("Delete ${args.currentMovie.movieTitle}?")
                builder.setMessage("Are you sure you want to delete ${args.currentMovie.movieTitle}?")
                builder.create().show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // function to check that all required input fields are not empty
    private fun inputCheck(movieName: String, studioName: String, criticsRating: Editable): Boolean{
        return !TextUtils.isEmpty(movieName) && !TextUtils.isEmpty(studioName) && criticsRating.isNotEmpty()
    }

    // resizes the uploaded image, to reduce the size stored on database
    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val resizedBitmap = Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * maxWidth.toFloat() / bitmap.height).toInt(),
            maxHeight,
            true
        )
        return resizedBitmap
    }
}