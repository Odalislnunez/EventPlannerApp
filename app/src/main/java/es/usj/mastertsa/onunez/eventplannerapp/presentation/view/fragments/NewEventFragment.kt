package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentNewEventBinding
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.EventsViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_ID
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.showToast
import java.sql.Timestamp
import java.util.*

@AndroidEntryPoint
class NewEventFragment : DialogFragment() {

    private var _binding: FragmentNewEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventsViewModel by viewModels()

    private var date: String = ""
    private var time: String = ""

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

    @SuppressLint("SimpleDateFormat")
    private fun initListeners(){
        binding.etDate.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    var dat = ""
                    if (monthOfYear + 1 > 9 && dayOfMonth > 9) {
                        dat = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                        date = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
                    }
                    else if (monthOfYear + 1 > 9 && dayOfMonth <= 9) {
                        dat = "0" + dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                        date = year.toString() + "-" + (monthOfYear + 1) + "-0" + dayOfMonth
                    }
                    else if (monthOfYear + 1 <= 9 && dayOfMonth > 9) {
                        dat = dayOfMonth.toString() + "/0" + (monthOfYear + 1) + "/" + year
                        date = year.toString() + "-0" + (monthOfYear + 1) + "-" + dayOfMonth
                    }
                    else {
                        dat = "0" + dayOfMonth.toString() + "/0" + (monthOfYear + 1) + "/" + year
                        date = year.toString() + "-0" +(monthOfYear + 1) + "-0" + dayOfMonth
                    }
                    binding.etDate.setText(dat)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        binding.etTime.setOnClickListener {
            val c = Calendar.getInstance()

            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { view, hourOfDay, minute ->
                    var tim = ""
                    if (minute > 9 && hourOfDay > 9) {
                        tim = "$hourOfDay:$minute"
                        time = "$hourOfDay:$minute:00.123456789"
                    }
                    else if (minute > 9 && hourOfDay <= 9) {
                        tim = "$hourOfDay:0$minute"
                        time = "$hourOfDay:0$minute:00.123456789"
                    }
                    else if (minute <= 9 && hourOfDay > 9) {
                        tim = "0$hourOfDay:$minute"
                        time = "0$hourOfDay:$minute:00.123456789"
                    }
                    else {
                        tim = "0$hourOfDay:0$minute"
                        time = "0$hourOfDay:0$minute:00.123456789"
                    }
                    binding.etTime.setText(tim)
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        }

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

//                val date = Timestamp.valueOf("$date $time")
                val date = "$date $time"

                if (binding.spParticipants.text.toString().isNotEmpty()) {
                    viewModel.saveEvent(
                        Event(
                            title = binding.etTitle.text.toString(),
                            description = binding.etDescription.text.toString(),
                            place = binding.etPlace.text.toString(),
                            datetime = date,
                            type = binding.spEventType.selectedItemPosition,
                            creators = getAllCreators(),
                            participants = getAllParticipants(),
                            status =  0
                        )
                    )
                }
                else {
                    viewModel.saveEvent(
                        Event(
                            title = binding.etTitle.text.toString(),
                            description = binding.etDescription.text.toString(),
                            place = binding.etPlace.text.toString(),
                            datetime = date,
                            type = binding.spEventType.selectedItemPosition,
                            creators = getAllCreators(),
                            status = 0
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
                && !binding.etPlace.text.isNullOrEmpty() && !binding.etDate.text.isNullOrEmpty() && !binding.etTime.text.isNullOrEmpty()
    }

    private fun hideProgressDialog() {
        binding.btnCreate.isEnabled = true
    }

    private fun showProgressBar() {
        binding.btnCreate.isEnabled = false
    }

}