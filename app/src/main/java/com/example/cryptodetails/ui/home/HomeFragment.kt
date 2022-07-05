package com.example.cryptodetails.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.cryptodetails.R
import com.example.cryptodetails.databinding.FragmentHomeBinding
import com.example.cryptodetails.model.Currency

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
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.viewModel = homeViewModel

        binding.outlinedTextField.setEndIconOnClickListener {
            val navController =
                (requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment).navController
            navController.navigate(R.id.navigation_my_account)
        }

        homeViewModel.getCurrenciesRepository().observe(viewLifecycleOwner) { data ->
            val currencyAdapter = CurrencyListAdapter(
                requireContext(),
                0,
                androidx.appcompat.R.layout.select_dialog_item_material,
                data
            )

            binding.searchBar.setAdapter(currencyAdapter)
            binding.searchBar.setOnItemClickListener { parent, view, position, id ->
                Log.d("posiiton is", position.toString())
                binding.codeValue.text = (parent.adapter.getItem(position) as Currency).name
                binding.nameValue.text = (parent.adapter.getItem(position) as Currency).fullName
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}