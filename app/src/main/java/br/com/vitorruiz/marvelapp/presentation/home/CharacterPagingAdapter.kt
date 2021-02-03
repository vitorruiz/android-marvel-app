package br.com.vitorruiz.marvelapp.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import br.com.vitorruiz.marvelapp.databinding.CharacterListItemBinding
import br.com.vitorruiz.marvelapp.model.Character
import br.com.vitorruiz.marvelapp.presentation.common.CharacterViewHolder

class CharacterPagingAdapter(
    private val onSelected: (Character) -> Unit,
    private val onFavorite: (Character, Int) -> Unit
) : PagingDataAdapter<Character, CharacterViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val itemBinding =
            CharacterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, position, onSelected, onFavorite) }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }
        }
    }

}