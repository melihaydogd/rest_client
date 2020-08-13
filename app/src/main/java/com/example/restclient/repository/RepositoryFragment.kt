package com.example.restclient.repository

import android.os.Bundle
import android.telephony.SmsMessage
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.restclient.R
import com.example.restclient.databinding.FragmentRepositoryBinding
import com.example.restclient.network.TimeFilter
import com.example.restclient.network.languages


class RepositoryFragment : Fragment() {

    private val viewModel: RepositoryViewModel by lazy {
        ViewModelProviders.of(this).get(RepositoryViewModel::class.java)
    }

    private lateinit var binding: FragmentRepositoryBinding
    private lateinit var adapter: RepositoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRepositoryBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = RepositoryAdapter(RepositoryAdapter.OnClickListener {
            viewModel.getDetails(it)
        })
        binding.repositoriesRecycle.adapter = adapter

        viewModel.details.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                this.findNavController().navigate(RepositoryFragmentDirections.actionRepositoryFragmentToDetailFragment(it))
                viewModel.getDetailsDone()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.title == "Show All") {
            viewModel.updateLanguageFilter("")
            return true
        }

        if(languages.contains(item.title)) {
            viewModel.updateLanguageFilter(item.title.toString())
            return true
        }

        if(item.itemId == R.id.daily) {
            viewModel.updateTimeFilter(TimeFilter.DAILY)
        } else if(item.itemId == R.id.weekly) {
            viewModel.updateTimeFilter(TimeFilter.WEEKLY)
        } else if(item.itemId == R.id.monthly) {
            viewModel.updateTimeFilter(TimeFilter.MONTHLY)
        } else {
            return false
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        val subMenu = menu!!.findItem(R.id.language_menu).subMenu
        viewModel.menu = subMenu
        val searchItem = menu!!.findItem(R.id.action_search)
        viewModel.actionSearch(searchItem, adapter)
        super.onCreateOptionsMenu(menu, inflater)
    }
}