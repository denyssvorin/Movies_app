package com.example.moviesapp.movielist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.R
import com.example.moviesapp.data.Movie
import com.example.moviesapp.data.SortOrder
import com.example.moviesapp.databinding.FragmentMovieListBinding
import com.example.moviesapp.utils.ImageExtractor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment(), MoviesAdapter.OnMovieClickListener {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MovieListViewModel>()

    private val moviesAdapter: MoviesAdapter by lazy { MoviesAdapter(this, requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        setupToolbarMenu()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcViewMovieList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moviesAdapter
        }
        if (viewModel.movies.value.isNullOrEmpty()){
            viewModel.loadData()
        }

        viewModel.movies.observe(viewLifecycleOwner) { movieList ->
            Log.i("TAG", "onViewCreated: movies observer")
            val moviesUIList = mutableListOf<Movie>()

            movieList.forEach { movie: Movie ->
                val image = ImageExtractor.getImagesFromAssets(movie.title, requireContext())
                movie.photo = image
                moviesUIList.add(movie)
            }

            moviesAdapter.submitList(moviesUIList)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.movieEvent.collect() { event ->
                when (event) {
                    is MovieListViewModel.MovieEvent.NavigateToMovieDetailsScreen -> {
                        val action =
                            MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(
                                event.movie
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun setupToolbarMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                val sortIcon = menu.findItem(R.id.action_sort)
                sortIcon.isVisible = true
                sortIcon.setIcon(null)
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_sort_by_title -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_TITLE)
                        return true
                    }

                    R.id.action_sort_by_release_date -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                        return true
                    }
                }
                return false
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMovieClick(movie: Movie) {
        viewModel.onMovieClick(movie)
    }
}