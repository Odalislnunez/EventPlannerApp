package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentNewEventBinding
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.EventsViewModel
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.NewEventViewModel

class NewEventFragment : DialogFragment() {

    private var _binding: FragmentNewEventBinding? = null
    private val binding get() = _binding!!
    val viewModel: EventsViewModel by viewModels()
//    lateinit var creatorUser: User

    companion object {
        fun newInstance() = NewEventFragment()
    }

//    private lateinit var viewModel: EventsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val newEventViewModel =
            ViewModelProvider(this)[NewEventViewModel::class.java]

        _binding = FragmentNewEventBinding.inflate(inflater, container, false)
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

        }

        return binding.root
    }

}