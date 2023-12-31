package com.manjil.movieapp.ui.feature.searchPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.manjil.movieapp.databinding.FragmentSearchBinding
import com.manjil.movieapp.ui.feature.detailsPage.DetailsActivity
import com.manjil.movieapp.ui.interfaces.ItemOnClickListener
import com.manjil.movieapp.domain.entities.DataItem
import com.manjil.movieapp.ui.MainViewModel
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), ItemOnClickListener {
    private lateinit var binding: FragmentSearchBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private var dataItemList: List<DataItem?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTabLayout()

//        mainViewModel.weatherData.observe(viewLifecycleOwner) {
//            dataItemList = it?.data
//            setMovieListAdapter(dataItemList)
//        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.weatherData.collect {
                    dataItemList = it.data
                    setMovieListAdapter(dataItemList)
                }
            }
        }

        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            if (dataItemList != null)
                setMovieListAdapter(filterData(text.toString().lowercase()))
        }
    }

    //returns the list filtered according to key
    private fun filterData(key: String): List<DataItem?> {
        return dataItemList!!.filter {
            it?.weather?.description!!.lowercase().contains(key)
        }
    }

    private fun setTabLayout() {
        binding.tlGenre.addTab(binding.tlGenre.newTab().setText("Fantasy"), true)
        binding.tlGenre.addTab(binding.tlGenre.newTab().setText("Horror"))
        binding.tlGenre.addTab(binding.tlGenre.newTab().setText("Science Fiction"))
        binding.tlGenre.addTab(binding.tlGenre.newTab().setText("Documentation"))
    }

    private fun setMovieListAdapter(list: List<DataItem?>?) {
        if (list != null) {
            if (list.isNotEmpty()) {
                binding.tvResultNotFound.visibility = View.GONE
                binding.rvMovieList.visibility = View.VISIBLE

                val adapter = MovieListAdapter(list, this, requireContext())
                val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                binding.rvMovieList.layoutManager = layoutManager
                binding.rvMovieList.adapter = adapter
            } else {
                binding.tvResultNotFound.visibility = View.VISIBLE
                binding.rvMovieList.visibility = View.GONE
            }
        }
    }

    override fun onItemClick(dataItemList: List<DataItem?>?, position: Int) {
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra("movie", dataItemList as ArrayList)
        intent.putExtra("position", position)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("bottomNav", "onDestroy: searchFragment")
    }
}