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
            // FIXME: not able to navigate back to home by tapping on home in the tab options after tapping on profile icon.
            val navController =
                (requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment).navController
            navController.navigate(R.id.navigation_my_account)
        }

//        lifecycleScope.launchWhenCreated {
//            homeViewModel.triggerCurrencyFlow().collectLatest {
//                // set adapter with the new data
//            }
//        }
//        lifecycleScope.launchWhenCreated {
//            homeViewModel.currencySharedFlow.collectLatest {
//                // set ada[ter with the new data
//                // will have the latest data here
//            }
//        }

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
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}