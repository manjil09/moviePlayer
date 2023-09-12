package com.manjil.movieapp.feature.detailsPage

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manjil.movieapp.databinding.ItemRelatedMovieBinding
import com.manjil.movieapp.model.MoviePojo

class RelatedMovieListAdapter(private val moviePojoList: ArrayList<MoviePojo>): RecyclerView.Adapter<RelatedMovieListAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemRelatedMovieBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRelatedMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return moviePojoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = moviePojoList[position]

        val spannable = SpannableString("${movie.title} (${movie.releaseYear})")
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#7F7E88")),
            movie.title.length+1,
            spannable.length,
            0
        )
        holder.binding.tvMovieTitle.setText(spannable, TextView.BufferType.SPANNABLE)
        holder.binding.ivMoviePoster.setImageResource(movie.poster)
    }
}