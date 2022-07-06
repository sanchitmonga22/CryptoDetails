package com.example.cryptodetails.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.cryptodetails.R
import com.example.cryptodetails.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

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
            // FIXME: not able to navigate back to home by tapping on home in the tab options after tapping on profile icon.
            val navController =
                (requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment).navController
            navController.navigate(R.id.navigation_my_account)
        }

        // will set the adapter after the data has been loaded
        homeViewModel.getCurrenciesRepository().observe(viewLifecycleOwner) { data ->
            binding.searchBar.setAdapter(CurrencyListAdapter(requireContext(), data))
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}