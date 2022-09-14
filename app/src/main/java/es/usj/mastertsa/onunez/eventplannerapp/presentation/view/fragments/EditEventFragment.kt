package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentEditEventBinding
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Invitation
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.EventsViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.EXTRAS_CHAT_ID
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.EXTRAS_CHAT_USER_ID
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
    private var invitationAnswer: Int = 0
    private val invitationsList: MutableList<String> = mutableListOf()

    private var contacts: MutableList<User> = mutableListOf()
    private var participantsList: MutableList<String> = mutableListOf()
    private var contactsArray: Array<String> = arrayOf()
    private lateinit var checkContact: BooleanArray

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

        viewModel.getInvitationsList(mEvent.eventId)

        initView()
        initObservers()
        initListeners()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initView(){
        if (mEvent.title.isNotEmpty()){
            val mDate: List<String> = mEvent.datetime.split(" ").toList()
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
            viewModel.getEventCreators(mEvent.creators)

            if (mEvent.type == 1) {
                binding.tvParticipants.isVisible = false
                binding.spParticipants.isVisible = false
                binding.btnChat.isVisible = false
            }
            else {
                if(mEvent.participants?.isNotEmpty() == true) {
                    binding.spParticipants.text = ""
                    viewModel.getEventParticipants(mEvent.participants!!)
                }
            }

            if(mEvent.creators.contains(USER_LOGGED_IN_ID)) {
                binding.btnSave.visibility = View.VISIBLE
                val datet = Timestamp.valueOf(mEvent.datetime)
                if(datet < Timestamp(System.currentTimeMillis())) {
                    binding.btnSave.isEnabled = false
                }
            }
            else {
                if (mEvent.type == 0) {
                    viewModel.getInvitationEvent(USER_LOGGED_IN_ID, mEvent.eventId)
                }
                else {
                    if(mEvent.participants?.contains(USER_LOGGED_IN_ID) == true) {
                        binding.btnParticipate.text = getString(R.string.button_participating)
                    }
                    else {
                        binding.btnParticipate.text = getString(R.string.button_participate)
                    }
                }
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

    @SuppressLint("SetTextI18n")
    private fun initObservers(){
        viewModel.saveEventState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideProgressDialog()
                    initView()
                    activity?.showToast(getString(R.string.everything_correctly_saved))
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    activity?.showToast(getString(R.string.error_something_went_wrong) + " Save")
                }
                else -> Unit
            }
        })

        viewModel.getInvitationEventState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideProgressDialog()
                    invitationAnswer = dataState.data.answer
                    if(dataState.data.answer == 0) {
                        binding.btnParticipate.text = getString(R.string.button_participate)
                        binding.btnChat.isVisible = false
                    }
                    else if (dataState.data.answer == 1) {
                        binding.btnParticipate.text = getString(R.string.button_participating)
                        binding.btnChat.isVisible = true
                    }
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    activity?.showToast(getString(R.string.error_something_went_wrong) + " Getting Invitation")
                }
                else -> Unit
            }
        })

        viewModel.getInvitationsListState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideProgressDialog()
                    invitationsList.addAll(dataState.data)
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    activity?.showToast(getString(R.string.error_something_went_wrong) + " Getting Invitation")
                }
                else -> Unit
            }
        })

        viewModel.participateEventState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideProgressDialog()
                    activity?.showToast(getString(R.string.everything_correctly_saved))
                    binding.btnParticipate.text = getString(R.string.button_participating)
                    binding.btnChat.visibility = View.VISIBLE
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    activity?.showToast(getString(R.string.error_something_went_wrong) + " Getting Invitation")
                }
                else -> Unit
            }
        })

        viewModel.unparticipateEventState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideProgressDialog()
                    activity?.showToast(getString(R.string.everything_correctly_saved))
                    binding.btnParticipate.text = getString(R.string.button_participate)
                    binding.btnChat.visibility = View.GONE
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    activity?.showToast(getString(R.string.error_something_went_wrong) + " Getting Invitation")
                }
                else -> Unit
            }
        })

        viewModel.eventCreatorsState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideProgressDialog()
                    dataState.data.forEach {
                        if(it.userId == dataState.data.first().userId){
                            binding.spOwners.text = it.name + " " + it.lastname
                        }
                        else{
                            binding.spOwners.text = binding.spOwners.text.toString() + ", \n" + it.name + " " + it.lastname
                        }
                    }
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    activity?.showToast(getString(R.string.error_something_went_wrong) + " Creators")
                }
                else -> Unit
            }
        })

        viewModel.eventParticipantsState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideProgressDialog()
                    dataState.data.forEach {
                        if(it.userId == dataState.data.first().userId){
                            binding.spParticipants.text = it.name + " " + it.lastname
                        }
                        else{
                            binding.spParticipants.text = binding.spOwners.text.toString() + ", \n" + it.name + " " + it.lastname
                        }
                    }
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    activity?.showToast(getString(R.string.error_something_went_wrong) + " Participants")
                }
                else -> Unit
            }
        })

        viewModel.getUserContactState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideProgressDialog()
                    contacts.clear()
                    contacts.addAll(dataState.data)

                    if(contactsArray.isEmpty()) {
                        var a = 0
                        contacts.forEach {
                            contactsArray = addElement(contactsArray, it.name + " " + it.lastname)
                            a += 1
                        }

                        checkContact = BooleanArray(contacts.size)
                    }

                    val builder = AlertDialog.Builder(requireActivity())

                    builder.setTitle("Select participants")
                    builder.setMultiChoiceItems(contactsArray, checkContact) { dialog, which, isChecked ->
                        checkContact[which] = isChecked
                    }

                    for (i in contactsArray.indices) {
                        if(invitationsList.contains(contactsArray[i])) {
                            checkContact[i] = true
                        }
                    }

                    binding.spParticipants.text = ""
                    participantsList = mutableListOf()

                    builder.setPositiveButton("OK") { dialog, which ->
                        for (i in checkContact.indices) {
                            val checked = checkContact[i]

                            if (checked) {
                                if(i != checkContact.count() - 1) {
                                    binding.spParticipants.text =
                                        binding.spParticipants.text.toString() + contactsArray[i] + "\n"
                                }
                                else {
                                    binding.spParticipants.text =
                                        binding.spParticipants.text.toString() + contactsArray[i]
                                }
                                participantsList.add(contacts[i].userId)
                            }
                        }
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    activity?.showToast(getString(R.string.error_something_went_wrong))
                }
                else -> Unit
            }
        })
    }

    fun addElement(arr: Array<String>, element: String): Array<String> {
        val mutableArray = arr.toMutableList()
        mutableArray.add(element)
        return mutableArray.toTypedArray()
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

        binding.spParticipants.setOnClickListener {
            viewModel.getUserContact(USER_LOGGED_IN_ID)
        }

        binding.btnChat.setOnClickListener {
            val bundle = bundleOf(EXTRAS_CHAT_ID to mEvent.eventId, EXTRAS_CHAT_USER_ID to USER_LOGGED_IN_ID)
            findNavController().navigate(R.id.action_nav_edit_event_to_nav_chat, bundle)
        }

        binding.btnSave.setOnClickListener {
            if (isAllDataSet()){
                showProgressBar()

                val date = "$date $time"
                viewModel.saveEvent(
                    Event(
                        eventId = mEvent.eventId,
                        title = binding.tvTitle.text.toString(),
                        description = binding.etDescription.text.toString(),
                        place = binding.etPlace.text.toString(),
                        datetime = date,
                        type = binding.spEventType.selectedItemPosition,
                        creators = listOf(USER_LOGGED_IN_ID),
                        participants = mEvent.participants,
                        status = 0
                    ),
                    participantsList
                )
            } else {
                activity?.showToast(getString(R.string.add_fields_new_event))
            }
        }

        binding.btnParticipate.setOnClickListener {
            if (invitationAnswer == 0) {
                viewModel.participateEvent(USER_LOGGED_IN_ID, mEvent.eventId)
            }
            else {
                viewModel.unparticipateEvent(USER_LOGGED_IN_ID, mEvent.eventId)
            }
        }
    }

    private fun isAllDataSet(): Boolean {
        return !binding.etDescription.text.isNullOrEmpty() && !binding.etPlace.text.isNullOrEmpty() && !binding.etDate.text.isNullOrEmpty()
                && !binding.etTime.text.isNullOrEmpty()
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