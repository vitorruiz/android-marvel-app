package br.com.vitorruiz.marvelapp.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.vitorruiz.marvelapp.R
import br.com.vitorruiz.marvelapp.core.ui.BaseFragment
import br.com.vitorruiz.marvelapp.databinding.DetailFragmentBinding
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : BaseFragment<DetailFragmentBinding>() {

    private val mViewModel: DetailViewModel by viewModel()

    override fun getBindingComponent(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DetailFragmentBinding {
        return DetailFragmentBinding.inflate(inflater, container, false)
    }

    override fun initComponents(rootView: View, savedInstanceState: Bundle?) {
        arguments?.let {
            val character = DetailFragmentArgs.fromBundle(it).character
            binding.tvCharacterName.text = character.name
            binding.tvDescription.text =
                if (character.description.isNotEmpty()) character.description
                else getString(R.string.message_no_description)

            bindFavoriteImage(character.isFavorite)
            binding.ivFavorite.setOnClickListener {
                mViewModel.favoriteCharacter(
                    character,
                    !character.isFavorite
                )
            }

            Glide.with(requireContext())
                .load(character.imageUrl())
                .into(binding.ivCharacter)
        }

        mViewModel.favoriteEvent().observe(viewLifecycleOwner) {
            bindFavoriteImage(it.isFavorite)
            val message = if (it.isFavorite) getString(R.string.message_added_to_favorites, it.name)
            else getString(R.string.message_removed_from_favorites, it.name)

            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    private fun bindFavoriteImage(isFavorite: Boolean) {
        binding.ivFavorite.setImageResource(if (isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off)
    }
}