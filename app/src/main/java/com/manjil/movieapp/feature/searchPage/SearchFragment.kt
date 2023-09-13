package com.manjil.movieapp.feature.searchPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.manjil.movieapp.BaseViewModel
import com.manjil.movieapp.databinding.FragmentSearchBinding
import com.manjil.movieapp.feature.detailsPage.DetailsActivity
import com.manjil.movieapp.interfaces.ItemOnClickListener
import com.manjil.movieapp.model.DataItem

class SearchFragment : Fragment(), ItemOnClickListener {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: BaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[BaseViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTabLayout()

        viewModel.getMovieList()
        viewModel.getWeatherData(27.7172,85.324)
        viewModel.weatherData.observe(viewLifecycleOwner) {
            setMovieListAdapter(it.data)
        }
//        viewModel.movieList.observe(viewLifecycleOwner){
//            setMovieListAdapter(it)
//        }
    }

    private fun setTabLayout() {
        binding.tlGenre.addTab(binding.tlGenre.newTab().setText("Fantasy"), true)
        binding.tlGenre.addTab(binding.tlGenre.newTab().setText("Horror"))
        binding.tlGenre.addTab(binding.tlGenre.newTab().setText("Science Fiction"))
        binding.tlGenre.addTab(binding.tlGenre.newTab().setText("Documentation"))
    }

    private fun setMovieListAdapter(
//        list: ArrayList<MoviePojo>,
        list: List<DataItem?>?
    ) {
        val adapter = MovieListAdapter(list, this, requireContext())
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        binding.rvMovieList.layoutManager = layoutManager
        binding.rvMovieList.adapter = adapter
    }

    override fun onItemClick(
//        dataItem: MoviePojo
        dataItem: DataItem
    ) {
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra("movie", dataItem)
        startActivity(intent)
    }

}