package com.example.restclient.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

val languages: ArrayList<String> = ArrayList()

@Parcelize
data class GithubRepository(
    val author: String,
    val name: String,
    val avatar: String,
    val language: String?,
    val stars: Int,
    val forks: Int,
    val currentPeriodStars: Int,
    val builtBy: List<User>) : Parcelable {

    val displayName = "$author/$name"
    val displayStar: String = stars.toString()

    init {
        addLanguage()
    }

    private fun addLanguage() {
       if(language != null && !languages.contains(language)) {
           languages.add(language)
       }
    }

}

@Parcelize
class User(
    val username: String,
    val avatar: String
) :Parcelable