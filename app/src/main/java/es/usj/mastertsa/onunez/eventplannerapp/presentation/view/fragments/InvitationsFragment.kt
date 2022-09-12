package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.os.Bundle
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
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentInvitationsBinding
import es.usj.mastertsa.onunez.eventplannerapp.presentation.view.adapters.EventAdapter
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.EventsViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_ID
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class InvitationsFragment : Fragment() {

    private var _binding: FragmentInvitationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventsViewModel by viewModels()

    @Inject
    lateinit var invitationsEventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvitationsBinding.inflate(inflater, container, false)
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
        viewModel.getInvitationsEvent(USER_LOGGED_IN_ID)
    }

    private fun initObservers(){
        viewModel.getInvitationsEventState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    invitationsEventAdapter.submitList(dataState.data)
                }
                is DataState.Error -> {
                    activity?.showToast(getString(R.string.error_something_went_wrong))
                }

                else -> Unit
            }
        })
    }

    private fun initRecyclerView() = binding.rvInvitations.apply {
        adapter = invitationsEventAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initListeners() {
        invitationsEventAdapter.setItemClickListener {
            val bundle = bundleOf(Constants.EXTRAS_EVENT to it)
            findNavController().navigate(R.id.action_nav_invitations_to_nav_edit_event, bundle)
        }
    }
}