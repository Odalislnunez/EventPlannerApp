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
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentEventsBinding
import es.usj.mastertsa.onunez.eventplannerapp.presentation.view.adapters.EventAdapter
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.EventsViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.EXTRAS_EVENT
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_ID
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventsViewModel by viewModels()

    @Inject
    lateinit var eventsAdapterUncoming: EventAdapter
    lateinit var eventsAdapterPast: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObservers()
        initRecyclerUncomingView()
        initRecyclerPastView()
        initListeners()
    }

    private fun init() {
        viewModel.getUncomingEvents(USER_LOGGED_IN_ID)
        viewModel.getPastEvents(USER_LOGGED_IN_ID)
    }

    private fun initObservers(){
        viewModel.uncomingEventState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    eventsAdapterUncoming.submitList(dataState.data)
                }
                is DataState.Error -> {
                    activity?.showToast(getString(R.string.error_something_went_wrong))
                }

                else -> Unit
            }
        })

        viewModel.pastEventState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    eventsAdapterPast.submitList(dataState.data)
                }
                is DataState.Error -> {
                    activity?.showToast(getString(R.string.error_something_went_wrong))
                }

                else -> Unit
            }
        })
    }

    private fun initRecyclerUncomingView() = binding.rvEvents.apply {
        adapter = eventsAdapterUncoming
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initRecyclerPastView() = binding.rvPastEvents.apply {
        adapter = eventsAdapterPast
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initListeners() {
        binding.btnAddEvent.setOnClickListener {
            findNavController().navigate(R.id.action_nav_events_to_nav_new_event)
        }

        eventsAdapterUncoming.setItemClickListener {
            val bundle = bundleOf(EXTRAS_EVENT to it)
            findNavController().navigate(R.id.action_nav_events_to_nav_edit_event, bundle )
        }

        eventsAdapterUncoming.setItemClickListener {
            val bundle = bundleOf(EXTRAS_EVENT to it)
            findNavController().navigate(R.id.action_nav_events_to_nav_edit_event, bundle )
        }
    }

}