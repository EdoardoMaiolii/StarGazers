package com.example.stargazers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.stargazers.databinding.FragmentHomeListUsersBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeListUsersFragment : Fragment() {

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

        homeListUsersViewModel.users.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { users ->
                //if the next page to ask is the second one we are doing a brand new search
                if (homeListUsersViewModel.getNextPage() == 2) {
                    homeUserAdapter.setNewUsers(users)
                } else {
                    homeUserAdapter.addUsers(users)
                }
            }
        }

        binding.searchButton.setOnClickListener {
            homeListUsersViewModel.setNextPage(1)
            homeListUsersViewModel.getStarGazersList(binding.edittextOwnerName.text.toString(), binding.edittextRepositoryName.text.toString(), null, null)
        }
    }
}