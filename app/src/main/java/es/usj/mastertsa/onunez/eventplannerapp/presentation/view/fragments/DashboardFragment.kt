package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentDashboardBinding
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.DashboardViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_NAME

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun initListeners() {
        binding.tvWelcome.text = getString(R.string.user_welcome) + " " + USER_LOGGED_IN_NAME + "!"

        binding.btnAddEvent.setOnClickListener {
            findNavController().navigate(R.id.action_nav_dashboard_to_nav_new_event)
        }
    }

}