package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentPublicEventsBinding
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.presentation.view.adapters.EventAdapter
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.EventsViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.showToast
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PublicEventsFragment : Fragment() {

    private var _binding: FragmentPublicEventsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventsViewModel by viewModels()

    var list: List<Event> = mutableListOf()

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

        init()
        initObservers()
        initRecyclerView()
        initListeners()
    }

    private fun init() {
        viewModel.getPublicEvents()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initObservers(){
        viewModel.publicEventState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Loading -> {
                    activity?.showToast("Loading")
                }
                is DataState.Success -> {
                    list = dataState.data
                    Log.e(TAG, list[0].title)
                    activity?.showToast(list[0].title)
                }
                is DataState.Error -> {
                    activity?.showToast(getString(R.string.error_something_went_wrong))
                }

                else -> Unit
            }
        })
    }

    private fun initRecyclerView() {
        viewModel.getPublicEvents()
        publicEventsAdapter.submitList(list)
        binding.rvPublicEvents.apply {
            adapter = publicEventsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initListeners() {
        viewModel.getPublicEvents()
        publicEventsAdapter.setItemClickListener {
            val bundle = bundleOf(Constants.EXTRAS_EVENT to it)
            findNavController().navigate(R.id.action_nav_events_to_nav_edit_event, bundle )
        }
    }
}