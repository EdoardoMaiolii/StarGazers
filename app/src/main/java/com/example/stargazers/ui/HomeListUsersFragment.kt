package com.example.stargazers.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.stargazers.R
import com.example.stargazers.SearchValidationResult
import com.example.stargazers.SearchValidationUtil
import com.example.stargazers.databinding.FragmentHomeListUsersBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeListUsersFragment : Fragment() {

    private var TAG = HomeListUsersFragment::class.simpleName

    private var _binding: FragmentHomeListUsersBinding? = null
    private val binding get() = _binding!!
    private var recyclerView: RecyclerView? = null

    private val homeListUsersViewModel: HomeListUsersViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeListUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeUserAdapter = HomeUserAdapter()

        recyclerView = binding.recyclerviewHomeUsers

        recyclerView?.apply {
            this.adapter = homeUserAdapter

            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    // Scroll ended, more users to load
                    if (!binding.recyclerviewHomeUsers.canScrollVertically(1)) {
                        homeListUsersViewModel.getStarGazersList(binding.edittextOwnerName.text.toString(), binding.edittextRepositoryName.text.toString(), null, null)
                    }
                }
            })
        }

        homeListUsersViewModel.errorModel.observe(viewLifecycleOwner) {
            Log.d(TAG, "text ${it.text}, code ${it.code}, message ${it.message}")
            Toast.makeText(requireContext(), getString(R.string.no_repo_found), Toast.LENGTH_LONG).show()
            binding.textviewDefault.visibility = View.VISIBLE
        }

        homeListUsersViewModel.loading.observe(viewLifecycleOwner) {
            binding.loader.visibility = if (it) View.VISIBLE else View.GONE
        }

        homeListUsersViewModel.users.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { users ->
                //if the next page to ask is the second one we are doing a brand new search
                if (homeListUsersViewModel.getNextPage() == 2) {
                    if (users.isNotEmpty()) {
                        binding.recyclerviewHomeUsers.visibility = View.VISIBLE
                        homeUserAdapter.setNewUsers(users)
                    } else {
                        binding.recyclerviewHomeUsers.visibility = View.GONE
                        binding.textviewDefault.visibility = View.VISIBLE
                        binding.textviewDefault.text = getString(R.string.repo_has_no_stargazers)
                    }
                } else {
                    homeUserAdapter.addUsers(users)
                }
            }
        }

        binding.searchButton.setOnClickListener {
            search()
        }

        binding.edittextRepositoryName.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.edittextRepositoryName.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })

        binding.edittextRepositoryName.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun hideKeyboard() {
        val view: View? = requireActivity().currentFocus
        if (view != null) {
            val input = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            input.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun search() {
        hideKeyboard()
        val ownerName = binding.edittextOwnerName.text.toString()
        val repositoryName = binding.edittextRepositoryName.text.toString()
        when (SearchValidationUtil.validateSearchInput(ownerName, repositoryName)) {
            SearchValidationResult.VALID -> {
                binding.textinputOwner.error = null
                binding.textinputRepository.error = null
                binding.textviewDefault.visibility = View.GONE
                binding.recyclerviewHomeUsers.visibility = View.GONE
                homeListUsersViewModel.setNextPage(1)
                homeListUsersViewModel.getStarGazersList(ownerName, repositoryName, null, null)
            }
            SearchValidationResult.OWNER_NOT_VALID ->  {
                binding.textinputOwner.error = "Inserire un owner"
                binding.textinputRepository.error = null
            }
            SearchValidationResult.REPOSITORY_NOT_VALID -> {
                binding.textinputRepository.error = "Inserire una repository"
                binding.textinputOwner.error = null
            }
            SearchValidationResult.OWNER_AND_REPO_NOT_VALID -> {
                binding.textinputOwner.error = "Inserire un owner"
                binding.textinputRepository.error = "Inserire una repository"
            }
        }
    }
}