package com.example.restclient.repository

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.restclient.databinding.ListItemBinding
import com.example.restclient.network.GithubRepository
import java.util.*
import kotlin.collections.ArrayList

class RepositoryAdapter(private val onClickListener: OnClickListener): ListAdapter<GithubRepository, RepositoryAdapter.RepositoryViewHolder>(DiffCallback), Filterable {

    private lateinit var filteredResults: MutableList<GithubRepository>

    init {
        filteredResults = currentList
        Log.i("RepositoryAdapter", filteredResults.toString())
    }

    companion object  DiffCallback: DiffUtil.ItemCallback<GithubRepository>() {
            override fun areItemsTheSame(
                oldItem: GithubRepository,
                newItem: GithubRepository
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: GithubRepository,
                newItem: GithubRepository
            ): Boolean {
                return (oldItem.author == newItem.author) && (oldItem.name == newItem.name)
            }
    }

    class RepositoryViewHolder(private var binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(githubRepository: GithubRepository) {
            binding.repository = githubRepository
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (githubRepository: GithubRepository) -> Unit) {
        fun onClick(githubRepository: GithubRepository) {
            clickListener(githubRepository)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        return RepositoryViewHolder(ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val githubRepository = filteredResults[position]
        holder.itemView.setOnClickListener {
            onClickListener.onClick(githubRepository)
        }
        holder.bind(githubRepository)
    }

    override fun getItemCount(): Int {
        return filteredResults.size
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val searchString = p0.toString().toLowerCase(Locale.getDefault())
                if(searchString.isEmpty()) {
                    filteredResults = currentList
                } else {
                    val temp = ArrayList<GithubRepository>()
                    for(repository in currentList) {
                        for(user in repository.builtBy) {
                            if (user.username.toLowerCase(Locale.getDefault()).contains(searchString)) {
                                if (!temp.contains(repository)) {
                                    temp.add(repository)
                                }
                            }
                        }
                    }
                    filteredResults = temp
                }
                val filterResults = FilterResults()
                filterResults.values = filteredResults
                filterResults.count = filteredResults.size
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredResults = p1!!.values as MutableList<GithubRepository>
                notifyDataSetChanged()
            }

        }
    }


}

