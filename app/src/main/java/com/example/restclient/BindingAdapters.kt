package com.example.restclient

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restclient.network.GithubRepository
import com.example.restclient.repository.GithubApiStatus
import com.example.restclient.repository.RepositoryAdapter

//The repositories are added to adapter. It is connected to RecyclerView via XML attribute.
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<GithubRepository>?) {
    val adapter = recyclerView.adapter as RepositoryAdapter
    adapter.submitList(data)
}

//For user's avatar.
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}

@BindingAdapter("githubApiStatus")
fun bindStatus(progressBar: ProgressBar, status: GithubApiStatus?) {
    when (status) {
        GithubApiStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
        }
        GithubApiStatus.ERROR -> {
            progressBar.visibility = View.GONE
        }
        GithubApiStatus.DONE -> {
            progressBar.visibility = View.GONE
        }
    }
}

@BindingAdapter("githubApiStatusText")
fun bindStatus(textView: TextView, status: GithubApiStatus?) {
    when (status) {
        GithubApiStatus.ERROR -> {
            textView.visibility = View.VISIBLE
            textView.text = "No Connection"
        }
       else -> textView.visibility = View.GONE
    }
}

@BindingAdapter("languageText")
fun bindLanguage(textView: TextView, language: String?) {
    if(language != null) {
        textView.text = language
    } else {
        textView.visibility = View.GONE
    }
}