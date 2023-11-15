package com.example.upschoolcapstoneproject.ui.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.upschoolcapstoneproject.common.viewBinding
import com.example.upschoolcapstoneproject.R
import com.example.upschoolcapstoneproject.common.gone
import com.example.upschoolcapstoneproject.common.visible
import com.example.upschoolcapstoneproject.databinding.FragmentSearchBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)

    private val viewModel by viewModels<SearchViewModel>()

    private val searchProductAdapter = SearchProductAdapter(onProductClick = ::onProductClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvSearch.adapter = searchProductAdapter

            with(viewModel) {
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText != null) {
                            if (newText.length >= 3) {
                                searchProduct(newText)
                            } else {
                                searchProductAdapter.submitList(emptyList())
                            }
                        }
                        return false
                    }
                })
            }
        }
        observeData()

    }

    private fun observeData() = with(binding) {
        viewModel.searchState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchState.Loading -> progressBar.visible()

                is SearchState.SuccesState -> {
                    progressBar.gone()
                    ivEmpty.gone()
                    tvEmpty.gone()
                    searchProductAdapter.submitList(state.products)
                }

                is SearchState.EmptyScreen -> {
                    progressBar.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    tvEmpty.text = state.failMessage
                    rvSearch.gone()
                }

                is SearchState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }

    private fun onProductClick(id: Int) {
        findNavController().navigate(SearchFragmentDirections.searchToDetail(id))
    }
}