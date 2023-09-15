package com.manjil.movieapp.feature.homePage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manjil.movieapp.R
import com.manjil.movieapp.databinding.ItemTrendingMovieBinding
import com.manjil.movieapp.interfaces.ItemOnClickListener
import com.manjil.movieapp.model.DataItem

class TrendingSliderAdapter(private val trendingMovieList: List<DataItem?>?, private val onClickListener: ItemOnClickListener, private val context: Context): RecyclerView.Adapter<TrendingSliderAdapter.ViewHolder>() {
    private val iconPath = "https://cdn.weatherbit.io/static/img/icons/"

    class ViewHolder(val binding: ItemTrendingMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrendingMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return trendingMovieList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = trendingMovieList!![position]
        holder.binding.tvMovieTitle.text = data?.weather?.description
        Glide
            .with(context)
            .load("$iconPath${data?.weather?.icon}.png")
            .placeholder(R.drawable.img_placeholder)
            .into(holder.binding.ivTrendingPoster)
        holder.binding.tvMovieRating.text = data?.temp.toString()

        holder.binding.trendingMovieContainer.setOnClickListener{
            onClickListener.onItemClick(trendingMovieList,position)
        }
    }
}