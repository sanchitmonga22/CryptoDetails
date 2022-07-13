package com.example.cryptodetails.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.cryptodetails.R
import com.example.cryptodetails.databinding.FragmentHomeBinding
import com.example.cryptodetails.ui.adapters.CurrencyListAdapter
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment() {
    // TODO: setup navigation through VM

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this@HomeFragment
        binding.viewModel = homeViewModel

        binding.outlinedTextField.setEndIconOnClickListener {
            navigateToMyAccount()
        }

        setupVM(homeViewModel)
        return binding.root
    }

    private fun setupVM(homeViewModel: HomeViewModel) {
        homeViewModel.makeAPICall()
        // will set the adapter after the data has been loaded
        lifecycleScope.launchWhenCreated {
            homeViewModel.currencyStateFlow.collectLatest {
                if (it == null) {
                    Toast.makeText(
                        context,
                        "An ERROR occurred, please check the network connection and try again later",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    binding.searchBar.setAdapter(CurrencyListAdapter(requireContext(), it))
                }
            }
        }
    }

    private fun navigateToMyAccount() {
        val navController =
            (requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment).navController
//        navController.backQueue
//        navController.graph.findNode(R.id.navigation_my_account)
        // FIXME: the state of R.id.navigation_home not being preserved when navigating back
        navController.popBackStack(R.id.navigation_home, true)
        navController.navigate(R.id.navigation_my_account)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}