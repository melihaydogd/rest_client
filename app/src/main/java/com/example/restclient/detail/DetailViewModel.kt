package com.example.restclient.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.restclient.R
import com.example.restclient.network.GithubRepository

class DetailViewModel(githubRepository: GithubRepository, app: Application) :
    AndroidViewModel(app) {

    private val _repository = MutableLiveData<GithubRepository>()
    val repository: LiveData<GithubRepository>
        get() = _repository

    init {
        _repository.value = githubRepository
    }

    val displayPeriodStars = Transformations.map(_repository) {
        app.applicationContext.getString(
            R.string.display_period_stars, it.currentPeriodStars
        )
    }

    val displayStars = Transformations.map(_repository) {
        app.applicationContext.getString(
            R.string.display_stars, it.stars
        )
    }

    val displayForks = Transformations.map(_repository) {
        app.applicationContext.getString(
            R.string.display_forks, it.forks
        )
    }
}