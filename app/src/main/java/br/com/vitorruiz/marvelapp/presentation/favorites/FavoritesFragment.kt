package br.com.vitorruiz.marvelapp.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.vitorruiz.marvelapp.R
import br.com.vitorruiz.marvelapp.core.ui.BaseFragment
import br.com.vitorruiz.marvelapp.databinding.FavoritesFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : BaseFragment<FavoritesFragmentBinding>() {

    private val mViewModel: FavoritesViewModel by viewModel()

    override fun getBindingComponent(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FavoritesFragmentBinding {
        return FavoritesFragmentBinding.inflate(inflater, container, false)
    }

    override fun initComponents(rootView: View, savedInstanceState: Bundle?) {
        binding.emptyView.ivEmptyImage.setImageResource(R.drawable.ic_favorite_off)
        binding.emptyView.tvEmptyText.text = getString(R.string.message_no_favorites)
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        mViewModel.favoritesList().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                showEmptyView()
            } else {
                showList()
            }
            binding.rvFavorites.adapter = CharacterAdapter(it,
                onSelected = {
                    findNavController().navigate(
                        FavoritesFragmentDirections.actionFavoritesFragmentToDetailFragment(it)
                    )
                },
                onFavorite = { character, position ->
                    mViewModel.removeFavorite(character)
                })
        }

        mViewModel.favoriteEvent().observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.message_removed_from_favorites, it.name),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun showEmptyView() {
        binding.rvFavorites.visibility = View.GONE
        binding.emptyView.emptyViewContainer.visibility = View.VISIBLE
    }

    private fun showList() {
        binding.rvFavorites.visibility = View.VISIBLE
        binding.emptyView.emptyViewContainer.visibility = View.GONE
    }
}