package br.com.vitorruiz.marvelapp.presentation.common

import androidx.recyclerview.widget.RecyclerView
import br.com.vitorruiz.marvelapp.R
import br.com.vitorruiz.marvelapp.databinding.CharacterListItemBinding
import br.com.vitorruiz.marvelapp.model.Character
import com.bumptech.glide.Glide

class CharacterViewHolder(private val binding: CharacterListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        character: Character,
        position: Int,
        onSelected: (Character) -> Unit,
        onFavorite: (Character, Int) -> Unit
    ) {
        binding.tvCharacterName.text = character.name
        binding.ivFavorite.setImageResource(if (character.isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off)

        Glide.with(itemView.context)
            .load(character.imageUrl())
            .into(binding.ivCharacter)

        binding.ivFavorite.setOnClickListener { onFavorite(character, position) }
        binding.root.setOnClickListener { onSelected(character) }
    }
}