package com.yhishi.android_operation_check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yhishi.android_operation_check.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.toolbar.inflateMenu(R.menu.toolbar_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.cameraImageGallery -> {
                    true
                }
                else -> false
            }
        }

        lifecycleScope.launch {
            viewModel.normalPreferencesValue
                .filter {
                    it.isNotEmpty()
                }
                .collect { normalPreferencesValue ->
                    Toast.makeText(requireContext(), "normalPreferencesValue = $normalPreferencesValue", Toast.LENGTH_LONG).show()
                }
        }

        lifecycleScope.launch {
            viewModel.encryptedPreferencesValue
                .filter {
                    it.isNotEmpty()
                }
                .collect { encryptedPreferencesValue ->
                    Toast.makeText(requireContext(), "encryptedPreferencesValue = $encryptedPreferencesValue", Toast.LENGTH_LONG).show()
                }
        }

        lifecycleScope.launch {
            viewModel.preferencesDataStoreValue
                .filter {
                    it.isNotEmpty()
                }
                .collect { preferencesDataStoreValue ->
                    Toast.makeText(requireContext(), "preferencesDataStoreValue = $preferencesDataStoreValue", Toast.LENGTH_LONG).show()
                }
        }
    }
}
