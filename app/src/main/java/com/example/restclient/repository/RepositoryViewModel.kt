package com.example.restclient.repository

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.widget.EditText
import android.widget.SearchView
import androidx.core.view.get
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.restclient.R
import com.example.restclient.bindRecyclerView
import com.example.restclient.network.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.util.*
import javax.security.auth.callback.Callback
import kotlin.collections.ArrayList
import kotlin.reflect.jvm.internal.impl.resolve.constants.NullValue
import kotlin.String as String

enum class GithubApiStatus {LOADING, ERROR, DONE}

class RepositoryViewModel : ViewModel() {

//    private var _displayList = MutableLiveData<ArrayList<GithubRepository>>()
//    val displayList: LiveData<ArrayList<GithubRepository>>
//        get() = _displayList
//    private val  = ArrayList<GithubRepository>()
//    val displayList: ArrayList<GithubRepository>
//        get() = _displayList

    //To test getting JSON.
//    private val _test = MutableLiveData<String>()
//    val test: LiveData<String>
//        get() = _test

    private val _status = MutableLiveData<GithubApiStatus>()
    val status: LiveData<GithubApiStatus>
        get() = _status

    private val _repositories = MutableLiveData<List<GithubRepository>>()
    val repositories: LiveData<List<GithubRepository>>
        get() = _repositories

    private val _details = MutableLiveData<GithubRepository>()
    val details: LiveData<GithubRepository>
        get() = _details
    fun getDetails(githubRepository: GithubRepository) {
        _details.value = githubRepository
    }
    fun getDetailsDone() {
        _details.value = null
    }

    companion object {
        private lateinit var _menu: SubMenu
    }
    var menu: SubMenu
        get() = _menu
        set(value: SubMenu) {
            _menu = value
        }


    private var currentLanguageFilter = ""
    private var currentTimeFilter = TimeFilter.DAILY

    //Since we are working with coroutines, we begin by creating a job.
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    init {
        getRepositories(currentLanguageFilter, currentTimeFilter)
    }

    private fun getRepositories(languageFilter: String, timeFilter: TimeFilter) {
        coroutineScope.launch {
            var getRepository = GithubApi.retrofitService.getProperties(languageFilter, timeFilter.value)
            try {
                _status.value = GithubApiStatus.LOADING

                val result = getRepository.await()
                _status.value = GithubApiStatus.DONE
                _repositories.value = result


                _menu.clear()
                _menu.add("Show All")
                for (language in languages) {
                    _menu.add(language)
                }
            } catch (e: Exception) {
                System.out.println("here" + e.message)
                _status.value = GithubApiStatus.ERROR
                _repositories.value = ArrayList()
            }
        }
    }

    fun updateLanguageFilter(languageFilter: String) {
        getRepositories(languageFilter, currentTimeFilter)
        currentLanguageFilter = languageFilter
    }

    fun updateTimeFilter(timeFilter: TimeFilter) {
        getRepositories(currentLanguageFilter, timeFilter)
        currentTimeFilter = timeFilter
    }

    //To test getting JSON.
//    private fun getRepositories() {
//        GithubApi.retrofitService.getProperties().enqueue(object : Callback,
//            retrofit2.Callback<String> {
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                _test.value = "Failure: " + t.message
//            }
//
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                _test.value = response.body()
//            }
//        })
//        System.out.println("here")
//        System.out.println(_test.value)
//    }

    fun actionSearch(menuItem: MenuItem, adapter: RepositoryAdapter) {

        if(menuItem != null) {
            val searchView = menuItem.actionView as SearchView
            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    adapter.filter.filter(p0)
                    return true
                }

            })
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}