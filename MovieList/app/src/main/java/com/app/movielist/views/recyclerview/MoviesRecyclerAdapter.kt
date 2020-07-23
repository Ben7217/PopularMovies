package com.app.movielist.views.recyclerview

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.movielist.R
import com.app.movielist.extensions.inflate
import com.app.movielist.model.MovieDetails
import com.app.movielist.model.Result
import com.app.movielist.movieapi.ApiContract
import com.app.movielist.viewmodel.MovieViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.details_layout.view.*
import kotlin.collections.ArrayList

class MoviesRecyclerAdapter:
    RecyclerView.Adapter<MoviesRecyclerAdapter.ViewHolder>() {
    var movies: ArrayList<MovieDetails> = ArrayList()
    var movieViewModel: MovieViewModel? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val viewLayout = parent.inflate(R.layout.movie_details_view_holder)
        return ViewHolder(viewLayout)
    }


    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(movies[position])
    }

    fun setMovieDetailsList(movieDetails: Result) {
        movies.clear()
        movies.addAll(movieDetails.results)
        movies.sortedWith(compareBy {it.vote_average})
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v

        init {
            v.setOnClickListener(this)
        }


        override fun onClick(clickedView: View?) {
            movieViewModel?.setSelectedMovie(movies[this.adapterPosition])
            movieViewModel?.setShowDetailsDialog(true)
        }

        fun bindViews(movieDetails: MovieDetails) {
            Log.d("bmr", "binding views " + movieDetails.poster_path)
            val moviePoster : ImageView = view.findViewById(R.id.details_poster)
            Picasso.get().load(ApiContract.IMAGE_PREFIX + movieDetails.poster_path).into(moviePoster)
            val titleText : TextView = view.findViewById(R.id.movie_title)
            titleText.text = movieDetails.title
            val movieOverview : TextView = view.findViewById(R.id.movie_overview)
            movieOverview.text = movieDetails.overview
            val movieReleaseDate : TextView = view.findViewById(R.id.movie_release_date)
            val releaseDate = "Release Date: "
            movieReleaseDate.text = releaseDate + movieDetails.release_date
            val userRating : TextView = view.findViewById(R.id.user_rating)
            val userRatingTitle = "Average Rating: "
            userRating.text = userRatingTitle + movieDetails.vote_average.toString()
        }

    }

    fun initMovieViewModel(movieViewModel: MovieViewModel) {
        this.movieViewModel = movieViewModel
    }
}