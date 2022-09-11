package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentEditEventBinding
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.EventsViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.EXTRAS_EVENT
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_ID
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_NAME
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.showToast
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class EditEventFragment : Fragment() {

    private var _binding: FragmentEditEventBinding? = null
    private val binding get() = _binding!!

    private var mEvent: Event = Event()

    private val viewModel: EventsViewModel by viewModels()

    private var date: String = ""
    private var time: String = ""

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

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initView(){
        if (mEvent.title.isNotEmpty()){
//            val mDate = Date(mEvent.datetime.time)
            val mDate: List<String> = mEvent.datetime.split(" ").toList()
//            date = SimpleDateFormat("yyyy-MM-dd").parse(mDate.toString())?.toString() ?: ""
//            time = (SimpleDateFormat("HH:mm").parse(mDate.toString())?.toString() ?: "") + ":00.123456789"

            date = mDate[0]
            time = mDate[1]

            val dateEt = LocalDate.parse(mDate[0]).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val timeEt = LocalTime.parse(mDate[1]).format(DateTimeFormatter.ofPattern("HH:mm"))

            binding.tvTitle.text = mEvent.title
            binding.etDescription.setText(mEvent.description)
            binding.etPlace.setText(mEvent.place)
            binding.etDate.setText(dateEt)
            binding.etTime.setText(timeEt)
            binding.spEventType.setSelection(mEvent.type)
            binding.spOwners.text = ""
            mEvent.creators.forEach {
                if(it == mEvent.creators.first()){
                    binding.spOwners.text = it
                }
                else{
                    binding.spOwners.text = binding.spOwners.text.toString() + ", \n" + it
                }
            }
            binding.spParticipants.text = ""
            mEvent.participants?.forEach {
                if(it == mEvent.participants?.first()) {
                    binding.spParticipants.text = it
                }
                else {
                    binding.spParticipants.text = binding.spParticipants.text.toString() + ", \n" + it
                }
            }
//            val creators: List<String> = binding.spOwners.text.toString().split(",").toList()
            if(mEvent.creators.contains(USER_LOGGED_IN_ID)) {
                binding.btnSave.visibility = View.VISIBLE
                val datet = Timestamp.valueOf(mEvent.datetime)
                if(datet < Timestamp(System.currentTimeMillis())) {
                    binding.btnSave.isEnabled = false
                }
            }
            else {
                binding.btnParticipate.visibility = View.VISIBLE
                binding.etDescription.isEnabled = false
                binding.etPlace.isEnabled = false
                binding.etDate.isEnabled = false
                binding.etTime.isEnabled = false
                binding.spEventType.isEnabled = false
                binding.spOwners.isEnabled = false
                binding.spParticipants.isEnabled = false
            }
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

//        binding.spOwners.text = USER_LOGGED_IN_NAME

        binding.btnChat.setOnClickListener {

        }

        binding.btnSave.setOnClickListener {
            if (isAllDataSet()){
                showProgressBar()

//                val date = Timestamp.valueOf("$date $time")
                val date = "$date $time"

                if (binding.spParticipants.text.toString().isNotEmpty()) {
                    viewModel.saveEvent(
                        Event(
                            eventId = mEvent.eventId,
                            title = binding.tvTitle.text.toString(),
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
                            eventId = mEvent.eventId,
                            title = binding.tvTitle.text.toString(),
                            description = binding.etDescription.text.toString(),
                            place = binding.etPlace.text.toString(),
                            datetime = date,
                            type = binding.spEventType.selectedItemPosition,
                            creators = getAllCreators(),
                            status =  0
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