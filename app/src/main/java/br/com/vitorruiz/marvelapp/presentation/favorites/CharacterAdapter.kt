package br.com.vitorruiz.marvelapp.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.vitorruiz.marvelapp.databinding.CharacterListItemBinding
import br.com.vitorruiz.marvelapp.model.Character
import br.com.vitorruiz.marvelapp.presentation.common.CharacterViewHolder

class CharacterAdapter(private val list: List<Character>, private val onSelected: (Character) -> Unit, private val onFavorite: (Character, Int) -> Unit) :
    RecyclerView.Adapter<CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val itemBinding =
            CharacterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(list[position], position, onSelected, onFavorite)
    }

    override fun getItemCount(): Int = list.size
}