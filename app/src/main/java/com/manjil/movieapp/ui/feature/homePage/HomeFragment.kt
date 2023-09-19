package com.manjil.movieapp.ui.feature.homePage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.manjil.movieapp.ui.BaseViewModel
import com.manjil.movieapp.databinding.FragmentHomeBinding
import com.manjil.movieapp.ui.feature.detailsPage.DetailsActivity
import com.manjil.movieapp.ui.interfaces.ItemOnClickListener
import com.manjil.movieapp.domain.entities.DataItem
import com.manjil.movieapp.ui.MainViewModel
import com.manjil.movieapp.util.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment(), ItemOnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: BaseViewModel
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity())[BaseViewModel::class.java]
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        mainViewModel.weatherData.observe(viewLifecycleOwner) {
//            setMovieListAdapter(it?.data)
//            binding.progressBar.visibility = View.GONE
//        }
//        mainViewModel.errorMessage.observe(viewLifecycleOwner) {
//            showErrorMessage(it)
//        }
        mainViewModel.data.observe(viewLifecycleOwner){
            when(it){
                is Result.Success -> {

                    setMovieListAdapter(it.data.data)
                    binding.progressBar.visibility = View.GONE
                }

                is Result.Error -> showErrorMessage( it.error)

                null -> {showErrorMessage("Could not connect to the server.")
                    Log.d("getweather", "getWeatherData: null value")}
            }
        }
    }

    private fun setMovieListAdapter(list: List<DataItem?>?) {
        val adapter = TrendingSliderAdapter(list, this, requireContext())
        binding.viewPagerTrendingSlider.visibility = View.VISIBLE
        binding.tvResultNotFound.visibility = View.GONE

        binding.viewPagerTrendingSlider.adapter = adapter

        //making off screen item visible
        binding.viewPagerTrendingSlider.clipToPadding = false
        binding.viewPagerTrendingSlider.clipChildren = false
        binding.viewPagerTrendingSlider.offscreenPageLimit = 2
        binding.viewPagerTrendingSlider.getChildAt(0).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER

        //changes the size of offscreen item
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        binding.viewPagerTrendingSlider.setPageTransformer(compositePageTransformer)

    }

    override fun onItemClick(dataItemList: List<DataItem?>?, position: Int) {
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra("movie", dataItemList as ArrayList)
        intent.putExtra("position", position)
        startActivity(intent)
    }

    private fun showErrorMessage(message: String) {
        binding.viewPagerTrendingSlider.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.tvResultNotFound.visibility = View.VISIBLE
        binding.tvResultNotFound.text = message
    }
}