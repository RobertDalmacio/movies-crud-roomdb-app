package com.example.movie_database_app.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_database_app.R
import com.example.movie_database_app.model.Movie

class MovieListAdapter: RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    private var movieList = emptyList<Movie>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieTitle: TextView = itemView.findViewById(R.id.tvMovieTitle)
        val studioName: TextView = itemView.findViewById(R.id.tvStudioName)
        val criticsRating: TextView = itemView.findViewById(R.id.tvCriticsRating)
        val moviePoster: ImageView = itemView.findViewById(R.id.ivMoviePosterList)
        val constLayout: ConstraintLayout = itemView.findViewById(R.id.rowLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = movieList[position]
        holder.movieTitle.text = currentItem.movieTitle
        holder.studioName.text = currentItem.studioName
        holder.criticsRating.text = currentItem.criticsRating.toString()
        holder.moviePoster.setImageBitmap(currentItem.moviePoster)

        // custom row item
        holder.constLayout.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)

            // navigate to update fragment
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(movie: List<Movie>) {
        this.movieList = movie
        notifyDataSetChanged()
    }
}