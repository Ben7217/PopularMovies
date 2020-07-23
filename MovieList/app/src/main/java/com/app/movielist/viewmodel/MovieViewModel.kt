package com.app.movielist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.movielist.model.Genres
import com.app.movielist.model.MovieDetails

/**
 * This ViewModel is basically being used to pass the
 * selected movie into the DetailDialog, as well as trigger the showing of the
 * DetailDialog.
 */

class MovieViewModel : ViewModel() {
    private var selectedMovie: MutableLiveData<MovieDetails> = MutableLiveData()
    private var showDetailsDialog: MutableLiveData<Boolean> = MutableLiveData()
    private var movieGenresList: MutableLiveData<Genres> = MutableLiveData()

    fun  setSelectedMovie(selectedMovie: MovieDetails) {
        this.selectedMovie.value = selectedMovie
    }

    fun getSelectedMovie(): MutableLiveData<MovieDetails> {
        return selectedMovie
    }

    fun setShowDetailsDialog(showDialog: Boolean) {
        this.showDetailsDialog.value = showDialog
    }

    fun getShowDetailsDialog(): MutableLiveData<Boolean> {
        return showDetailsDialog
    }

    fun setMovieGenresList(genre: Genres) {
        this.movieGenresList.value = genre
    }

    fun getMovieGenresList(): MutableLiveData<Genres> {
        return movieGenresList
    }
}