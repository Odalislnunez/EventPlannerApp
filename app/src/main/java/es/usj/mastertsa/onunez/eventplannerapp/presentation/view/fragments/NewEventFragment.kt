package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentNewEventBinding
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.NewEventViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.EXTRAS_EVENT
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_ID
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.showToast

@AndroidEntryPoint
class NewEventFragment : DialogFragment() {

    private var _binding: FragmentNewEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
    }

    private fun initObservers(){
        viewModel.saveEventState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideProgressDialog()
                    activity?.onBackPressed()
                    activity?.showToast(getString(R.string.everything_correctly_saved))
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    activity?.showToast(getString(R.string.error_something_went_wrong))
                }
                else -> Unit
            }
        })
    }

    private fun initListeners(){
        val spinner: Spinner = binding.spEventType
        activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.event_type_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }

//        binding.spOwners.text = creatorUser.name + " " + creatorUser.lastname

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnCreate.setOnClickListener {
            if (isAllDataSet()){
                showProgressBar()
                if (binding.spParticipants.text.toString().isNotEmpty()) {
                    viewModel.saveEvent(
                        Event(
                            event_title = binding.etTitle.text.toString(),
                            event_description = binding.etDescription.text.toString(),
                            event_place = binding.etPlace.text.toString(),
                            event_date = binding.etDate.text.toString(),
                            event_type = binding.spEventType.selectedItemPosition,
                            event_creators = getAllCreators(),
                            event_participants = getAllParticipants()
                        )
                    )
                }
                else {
                    viewModel.saveEvent(
                        Event(
                            event_title = binding.etTitle.text.toString(),
                            event_description = binding.etDescription.text.toString(),
                            event_place = binding.etPlace.text.toString(),
                            event_date = binding.etDate.text.toString(),
                            event_type = binding.spEventType.selectedItemPosition,
                            event_creators = getAllCreators()
                        )
                    )
                }
            } else {
                activity?.showToast(getString(R.string.add_fields_new_event))
            }
        }
    }

    private fun getAllCreators(): List<String> {
        val creators: MutableList<String> = mutableListOf()

        if(binding.spOwners.text.toString().isNotEmpty()) {
            val creators_list: List<String> = binding.spOwners.text.toString().split(",").toList()

            creators.add(USER_LOGGED_IN_ID)
            creators_list.forEach { creator ->
                creators.add(creator)
            }
        }
        else {
            creators.add(USER_LOGGED_IN_ID)
        }
        return creators
    }

    private fun getAllParticipants(): List<String> {
        val participants: MutableList<String> = mutableListOf()
        val participants_list: List<String> = binding.spParticipants.text.toString().split(",").toList()

        participants_list.forEach { participant ->
            participants.add(participant)
        }

        return participants
    }

    private fun isAllDataSet(): Boolean {
        return !binding.etTitle.text.isNullOrEmpty() && !binding.etDescription.text.isNullOrEmpty()
                && !binding.etPlace.text.isNullOrEmpty() && !binding.etDate.text.isNullOrEmpty()
    }

    private fun hideProgressDialog() {
        binding.btnCreate.isEnabled = true
    }

    private fun showProgressBar() {
        binding.btnCreate.isEnabled = false
    }

}