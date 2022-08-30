package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentEditEventBinding
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.EditEventViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.EXTRAS_EVENT
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_ID
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.showToast

class EditEventFragment : Fragment() {

    private var _binding: FragmentEditEventBinding? = null
    private val binding get() = _binding!!

    private var mEvent: Event = Event()

    private val viewModel: EditEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mEvent = arguments?.getParcelable<Event>(EXTRAS_EVENT)?: Event()

        initView()
        initListeners()
        initObservers()
    }

    private fun initView(){
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

        if (!mEvent.event_title.isNullOrEmpty()){
            binding.tvTitle.text = mEvent.event_title
            binding.etDescription.setText(mEvent.event_description)
            binding.etPlace.setText(mEvent.event_place)
            binding.etDate.setText(mEvent.event_date)
            binding.spEventType.setSelection(mEvent.event_type)
            binding.spOwners.text = mEvent.event_creators.toString()
            binding.spParticipants.text = mEvent.event_participants.toString()

            binding.btnSave.visibility = View.VISIBLE

//            val creators: List<String> = binding.spOwners.text.toString().split(",").toList()
//            if(creators.contains(USER_LOGGED_IN_ID)) {
//                binding.btnSave.visibility = View.VISIBLE
//            }
//            else {
//                binding.btnParticipate.visibility = View.VISIBLE
//                binding.etDescription.isEnabled = false
//                binding.etPlace.isEnabled = false
//                binding.etDate.isEnabled = false
//                binding.spEventType.isEnabled = false
//                binding.spOwners.isEnabled = false
//                binding.spParticipants.isEnabled = false
//            }
        }
    }

    private fun initObservers(){
        viewModel.saveEventState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideProgressDialog()
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
        binding.btnChat.setOnClickListener {

        }

        binding.btnSave.setOnClickListener {
            if (isAllDataSet()){
                showProgressBar()
                if (binding.spParticipants.text.toString().isNotEmpty()) {
                    viewModel.saveEvent(
                        Event(
                            eventId = mEvent.eventId,
                            event_title = binding.tvTitle.text.toString(),
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
                            eventId = mEvent.eventId,
                            event_title = binding.tvTitle.text.toString(),
                            event_description = binding.etDescription.text.toString(),
                            event_place = binding.etPlace.text.toString(),
                            event_date = binding.etDate.text.toString(),
                            event_type = binding.spEventType.selectedItemPosition,
                            event_creators = getAllCreators()
                        )
                    )
                }
            } else {
                activity?.showToast(getString(R.string.add_fields_edit_event))
            }
        }

        binding.btnParticipate.setOnClickListener {

        }
    }

    private fun getAllCreators(): List<String> {
        val creators: MutableList<String> = mutableListOf()

        if(binding.spOwners.text.toString().isNotEmpty()) {
            val creators_list: List<String> = binding.spOwners.text.toString().split(",").toList()

            creators.add(Constants.USER_LOGGED_IN_ID)
            creators_list.forEach { creator ->
                creators.add(creator)
            }
        }
        else {
            creators.add(Constants.USER_LOGGED_IN_ID)
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
        return !binding.etDescription.text.isNullOrEmpty() && !binding.etPlace.text.isNullOrEmpty() && !binding.etDate.text.isNullOrEmpty()
    }

    private fun hideProgressDialog() {
        binding.btnSave.isEnabled = true
        binding.btnParticipate.isEnabled = true
    }

    private fun showProgressBar() {
        binding.btnSave.isEnabled = false
        binding.btnParticipate.isEnabled = false
    }
}