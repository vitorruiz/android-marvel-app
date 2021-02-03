package br.com.vitorruiz.marvelapp.presentation.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.vitorruiz.marvelapp.R
import br.com.vitorruiz.marvelapp.core.ui.AppLoaderStateAdapter
import br.com.vitorruiz.marvelapp.core.ui.BaseFragment
import br.com.vitorruiz.marvelapp.data.source.network.HttpErrorHandler
import br.com.vitorruiz.marvelapp.databinding.HomeFragmentBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    private val mViewModel: HomeViewModel by viewModel()
    private val mAdapter = CharacterPagingAdapter(
        {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(it)
            )
        },
        { character, position ->
            mViewModel.favoriteCharacter(character, !character.isFavorite, position)
        })

    private var mSearchJob: Job? = null

    override fun getBindingComponent(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): HomeFragmentBinding {
        return HomeFragmentBinding.inflate(inflater, container, false)
    }

    override fun settings() {
        setHasOptionsMenu(true)
        lifecycle.addObserver(mViewModel)
    }

    override fun initComponents(rootView: View, savedInstanceState: Bundle?) {
        binding.emptyView.ivEmptyImage.setImageResource(R.drawable.ic_search)
        binding.emptyView.tvEmptyText.text = getString(R.string.message_no_characters_founded)

        binding.rvCharacters.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter.withLoadStateFooter(AppLoaderStateAdapter { mAdapter.retry() })
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        binding.fbFavorites.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavoritesFragment())
        }

        lifecycleScope.launchWhenCreated {
            mAdapter.loadStateFlow.collectLatest {

                when (val refresher = it.refresh) {
                    is LoadState.Loading -> {
                        showLoading()
                    }
                    is LoadState.NotLoading -> {
                        if (mAdapter.itemCount == 0) {
                            showEmptyView()
                        } else {
                            showList()
                        }
                    }
                    is LoadState.Error -> {
                        HttpErrorHandler.resolveError(refresher.error,
                            onFail = {
                                showErrorView(getString(R.string.error_message_default), false)
                            },
                            onError = {
                                val message =
                                    if (it.isConnectionError) getString(R.string.error_message_no_connection)
                                    else getString(R.string.error_message_default)

                                showErrorView(message, it.isConnectionError)
                            })
                    }
                }
            }
        }

        mViewModel.pagingData().observe(viewLifecycleOwner) {
            mSearchJob = lifecycleScope.launch { mAdapter.submitData(it) }
        }

        mViewModel.favoriteResult().observe(viewLifecycleOwner) {
            mAdapter.notifyItemChanged(it.first)
            val message = if (it.second.isFavorite) getString(
                R.string.message_added_to_favorites,
                it.second.name
            )
            else getString(R.string.message_removed_from_favorites, it.second.name)
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        val menuItem = menu.findItem(R.id.app_bar_search)
        val searchView = SearchView(requireContext())
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
        menuItem.actionView = searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                search(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                search()
                return true
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun search(name: String? = null) {
        mSearchJob?.cancel()
        mViewModel.searchCharacters(name)
    }

    private fun showList() {
        binding.rvCharacters.visibility = View.VISIBLE
        binding.loadingView.loadingViewContainer.visibility = View.GONE
        binding.emptyView.emptyViewContainer.visibility = View.GONE
        binding.errorView.errorViewContainer.visibility = View.GONE
    }

    private fun showLoading() {
        binding.rvCharacters.visibility = View.GONE
        binding.loadingView.loadingViewContainer.visibility = View.VISIBLE
        binding.emptyView.emptyViewContainer.visibility = View.GONE
        binding.errorView.errorViewContainer.visibility = View.GONE
    }

    private fun showEmptyView() {
        binding.rvCharacters.visibility = View.GONE
        binding.loadingView.loadingViewContainer.visibility = View.GONE
        binding.emptyView.emptyViewContainer.visibility = View.VISIBLE
        binding.errorView.errorViewContainer.visibility = View.GONE
    }

    private fun showErrorView(message: String, isConnectionError: Boolean) {
        binding.rvCharacters.visibility = View.GONE
        binding.loadingView.loadingViewContainer.visibility = View.GONE
        binding.emptyView.emptyViewContainer.visibility = View.GONE
        binding.errorView.errorViewContainer.visibility = View.VISIBLE

        binding.errorView.tvErrorDescription.text = message
        binding.errorView.ivError.setImageResource(if (isConnectionError) R.drawable.ic_baseline_signal_wifi_off else R.drawable.ic_error)
    }
}