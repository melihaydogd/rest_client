package com.example.restclient.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.core.view.doOnAttach
import androidx.databinding.adapters.ToolbarBindingAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.restclient.R
import com.example.restclient.R.*
import com.example.restclient.bindImage
import com.example.restclient.databinding.FragmentDetailBinding
import com.example.restclient.detail.DetailFragmentArgs.fromBundle
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.user_view.view.*


class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = this


        //This comes from navigation.(navigation argument)
        val githubRepository = fromBundle(arguments!!).selectedRepository

        val viewModelFactory = DetailViewModelFactory(githubRepository, application)

        binding.viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)

        for(user in githubRepository.builtBy) {
            val view =
                inflater.inflate(R.layout.user_view, null)
            binding.root.scroll_view.linear_view.addView(view)
            bindImage(view.image, user.avatar)
            view.username_text.text = user.username
        }


        return binding.root
    }

}