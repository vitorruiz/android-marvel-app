package br.com.vitorruiz.marvelapp.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.vitorruiz.marvelapp.databinding.LayoutLoadingItemBinding

class AppLoaderStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<AppLoaderStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val itemBinding =
            LayoutLoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bindView(loadState, retry)
    }

    inner class LoadStateViewHolder(private val binding: LayoutLoadingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(loadState: LoadState, retry: () -> Unit) = with(itemView) {
            if (loadState is LoadState.Error) {
                binding.tvRetry.text = loadState.error.localizedMessage
            }
            binding.pbLoadingItem.visibility = toVisibility(loadState is LoadState.Loading)
            binding.btRetry.visibility = toVisibility(loadState !is LoadState.Loading)
            binding.tvRetry.visibility = toVisibility(loadState !is LoadState.Loading)

            binding.btRetry.setOnClickListener { retry.invoke() }
        }

        private fun toVisibility(constraint: Boolean): Int = if (constraint) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}