package com.manjil.movieapp.feature.detailsPage

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manjil.movieapp.R
import com.manjil.movieapp.databinding.ItemRelatedMovieBinding
import com.manjil.movieapp.interfaces.ItemOnClickListener
import com.manjil.movieapp.model.DataItem

class RelatedMovieListAdapter(
    private val movieList: List<DataItem?>?,
    private val context: Context,
    private val itemOnClickListener: ItemOnClickListener
) :
    RecyclerView.Adapter<RelatedMovieListAdapter.ViewHolder>() {
    private val iconPath = "https://cdn.weatherbit.io/static/img/icons/"

    class ViewHolder(var binding: ItemRelatedMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRelatedMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movieList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = movieList?.get(position)

        val title = dataItem?.weather?.description
        val releaseYear = dataItem?.validDate?.substring(0, 4)

        val spannable = SpannableString("$title ($releaseYear)")
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#7F7E88")),
            title!!.length + 1,
            spannable.length,
            0
        )
        holder.binding.tvMovieTitle.setText(spannable, TextView.BufferType.SPANNABLE)
        Glide.with(context)
            .load("$iconPath${dataItem.weather.icon}.png")
            .placeholder(R.drawable.img_placeholder)
            .into(holder.binding.ivMoviePoster)

        holder.binding.cvMovieBg.setOnClickListener {
            itemOnClickListener.onItemClick(movieList, position)
        }
    }
}