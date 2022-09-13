package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.EventsViewModel
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.UsersViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_ID
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_NAME
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.showToast
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class NewEventFragment : DialogFragment() {

    private var _binding: FragmentNewEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventsViewModel by viewModels()
    private val viewModelUser: UsersViewModel by viewModels()

    private var date: String = ""
    private var time: String = ""

    private var contacts: List<User> = mutableListOf()
    private lateinit var participantsList: MutableList<String>
    private var contactsArray: Array<String> = arrayOf()

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

        viewModelUser.getUserContactState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideProgressDialog()
                    contacts = dataState.data
                    var a = 0
                    contacts.forEach {
                        contactsArray[a] = it.name + " " + it.lastname
                        a += 1
                    }
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    activity?.showToast(getString(R.string.error_something_went_wrong))
                }
                else -> Unit
            }
        })
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
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
                        tim = "0$hourOfDay:$minute"
                        time = "0$hourOfDay:$minute:00.123456789"
                    }
                    else if (minute <= 9 && hourOfDay > 9) {
                        tim = "$hourOfDay:0$minute"
                        time = "$hourOfDay:0$minute:00.123456789"
                    }
                    else {
                        tim = "0$hourOfDay:0$minute"
                        time = "0$hourOfDay:0$minute:00.123456789"
                    }
                    binding.etTime.setText(tim)
                },
                hour,
                minute,
                true
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

        binding.spOwners.text = USER_LOGGED_IN_NAME

        binding.spParticipants.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())

            viewModelUser.getUserContact(USER_LOGGED_IN_ID)

            val checkContact = BooleanArray(contacts.size)

            builder.setTitle("Select participants")
            builder.setMultiChoiceItems(contactsArray, checkContact) { dialog, which, isChecked ->
                checkContact[which] = isChecked
            }
            builder.setPositiveButton("OK") { dialog, which ->
                for (i in checkContact.indices) {
                    val checked = checkContact[i]
                    binding.spParticipants.text = ""
                    if (checked) {
                        binding.spParticipants.text = binding.spParticipants.text.toString() + contactsArray[i] + "\n"
                        participantsList = mutableListOf()
                        participantsList.add(contacts[i].userId)
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.btnCancel.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btnCreate.setOnClickListener {
            if (isAllDataSet()){
                showProgressBar()

//                val date = Timestamp.valueOf("$date $time")
                val date = "$date $time"

                if (binding.spParticipants.text.toString().isNotEmpty()) {

                }
                else {

                }
                viewModel.saveEvent(
                    Event(
                        title = binding.etTitle.text.toString(),
                        description = binding.etDescription.text.toString(),
                        place = binding.etPlace.text.toString(),
                        datetime = date,
                        type = binding.spEventType.selectedItemPosition,
                        creators = listOf(USER_LOGGED_IN_ID),
                        status = 0
                    ),
                    mutableListOf()
                )
            } else {
                activity?.showToast(getString(R.string.add_fields_new_event))
            }
        }
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