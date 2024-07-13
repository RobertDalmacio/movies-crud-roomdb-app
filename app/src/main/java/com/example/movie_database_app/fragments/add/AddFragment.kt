package com.example.movie_database_app.fragments.add

import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.movie_database_app.R
import com.example.movie_database_app.model.Movie
import com.example.movie_database_app.viewmodel.MovieViewModel

class AddFragment : Fragment() {

    private lateinit var mMovieViewModel: MovieViewModel
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { view?.findViewById<ImageView>(R.id.ivMoviePoster)?.setImageURI(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        mMovieViewModel = ViewModelProvider(this)[MovieViewModel::class.java]

        // function that inserts data to database
        fun insertDataToDatabase() {
            val movieTitle = view.findViewById<EditText>(R.id.etMovieTitle).text.toString()
            val studioName = view.findViewById<EditText>(R.id.etStudioName).text.toString()
            val moviePoster = resizeBitmap(view.findViewById<ImageView>(R.id.ivMoviePoster).drawable.toBitmap(), 80, 100 )
            // set a default value of 0
            var criticsRating = 0
            // only convert to number id string is not empty
            if (view.findViewById<EditText>(R.id.etCriticsRating).text.isNotEmpty()) {
                criticsRating = view.findViewById<EditText>(R.id.etCriticsRating).text.toString().toDouble().toInt()
            }

            // checks if 3 required text fields are filled out
            if (inputCheck(movieTitle, studioName, view.findViewById<EditText>(R.id.etCriticsRating).text)) {
                // checks that critics rating is between 0 to 100
                if (criticsRating in 0..100) {
                    // create new movie
                    val movie = Movie(0, movieTitle, studioName, criticsRating, moviePoster)

                    // inserts to database
                    mMovieViewModel.addMovie(movie)

                    // show success message
                    Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()

                    // navigate back to list fragment
                    findNavController().navigate((R.id.action_addFragment_to_listFragment))
                } else {
                    Toast.makeText(requireContext(), "Please ensure rating is between 0 and 100.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_LONG).show()
            }
        }

        // add submit button
        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            insertDataToDatabase()
        }

        // select image button
        view.findViewById<Button>(R.id.btnSelectPoster).setOnClickListener {
            resultLauncher.launch("image/*")
        }

        return view
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