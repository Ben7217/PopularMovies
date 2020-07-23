package com.app.movielist.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.movielist.R
import com.app.movielist.model.MovieDetails
import com.app.movielist.movieapi.ApiContract
import com.app.movielist.movieservice.MovieService
import com.app.movielist.viewmodel.MovieViewModel
import com.app.movielist.views.recyclerview.MoviesRecyclerAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This class is the inital Fragment that instantiates the RecyclerView,
 * its adapter, and fetches the data from the API. I realize that this coupling is
 * less than ideal, but I ran out of time.
 */
class MovieRecyclerView : Fragment() {
    private val TAG: String = "MovieRecyclerView"
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var movieViewModel: MovieViewModel
    private var selectedMovie: MovieDetails? = null
    private lateinit var detailsView: DetailDialog
    private lateinit var recyclerView: RecyclerView
    val movieService by lazy {
        initService()
    }

    private fun initService(): MovieService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiContract.API_URL)
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.create()
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
        return retrofit.create(MovieService::class.java)
    }

    lateinit var movieAdapter: MoviesRecyclerAdapter
    var disposable: Disposable? = null
    var genreDisposable: Disposable? = null


    /**
     * Here I am observing MovieViewModels mutable live data functions
     * for any changes so we can react appropriately.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieViewModel = MovieViewModel()
        detailsView = DetailDialog()
        movieViewModel.getSelectedMovie().observe(this,
            Observer { result -> setSelectedMovie(result) })
        movieViewModel.getMovieGenresList().observe(this,
            Observer { genres -> detailsView.setGenreList(genres) })
        movieViewModel.getShowDetailsDialog().observe(this,
            Observer { show -> showDetailsDialog(detailsView, show) })
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.movies_list_fragment, container, false)
        recyclerView = view.findViewById(R.id.movies_recycler_view)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        movieAdapter = MoviesRecyclerAdapter()
        movieAdapter.initMovieViewModel(movieViewModel)
        recyclerView.adapter = movieAdapter

        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))


        //Ideally this would be in a helper class of some sort or maybe in the ViewModel
        disposable = movieService.getPopularMovies(ApiContract.API_KEY)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> movieAdapter.setMovieDetailsList(result) },
                { error ->
                    Log.e(
                        TAG, "Something went wrong retrieving movie list",
                        Exception(error)
                    )
                }
            )

        genreDisposable = movieService.getGenreList(ApiContract.API_KEY)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {result -> movieViewModel.setMovieGenresList(result)},
                {error -> Log.e(TAG, "Problem retrieving genre list",
                Exception(error))}
            )

        return view
    }

    override fun onResume() {
        super.onResume()
        selectedMovie?.let {
            setSelectedMovie(it)
        }

    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
        genreDisposable?.dispose()
        if (detailsView.isVisible) {
            detailsView.dismiss()
        }
    }

    private fun showDetailsDialog(detailsDialog: DetailDialog, show: Boolean) {
        if (show) {
            this.fragmentManager?.let { detailsDialog.show(it, null) }
        } else {
            this.fragmentManager?.let { detailsDialog.dismiss() }
        }
    }

    private fun setSelectedMovie(movie: MovieDetails) {
        detailsView.setMovie(movie)
        selectedMovie = movie
    }
}


