package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentPublicEventsBinding
import es.usj.mastertsa.onunez.eventplannerapp.presentation.view.adapters.EventAdapter
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.EventsViewModel
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.PublicEventsViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class PublicEventsFragment : Fragment() {

    private var _binding: FragmentPublicEventsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventsViewModel by viewModels()

    @Inject
    lateinit var publicEventsAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPublicEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        init()
//        initRecyclerView()
        initListeners()
    }

    private fun init() {
        viewModel.getPublicEvents()
    }

    private fun initObservers(){
        viewModel.publicEventState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    publicEventsAdapter.submitList(dataState.data)
                    binding.rvPublicEvents.apply {
                        adapter = publicEventsAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                }
                is DataState.Error -> {
                    activity?.showToast(getString(R.string.error_something_went_wrong))
                }

                else -> Unit
            }
        })
    }

    private fun initRecyclerView() = binding.rvPublicEvents.apply {
        adapter = publicEventsAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initListeners() {
        publicEventsAdapter.setItemClickListener {
            val bundle = bundleOf(Constants.EXTRAS_EVENT to it)
            findNavController().navigate(R.id.action_nav_events_to_nav_edit_event, bundle )
        }
    }
}