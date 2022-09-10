package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentAddContactBinding
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.presentation.view.adapters.ContactAdapter
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.UsersViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_ID
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class AddContactFragment : DialogFragment() {
    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UsersViewModel by viewModels()

    var mUser: User = User()

    @Inject
    lateinit var contactsAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
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
        viewModel.getUserNoContact(USER_LOGGED_IN_ID)
        viewModel.getUserInObjectData(USER_LOGGED_IN_ID)
    }

    private fun initObservers(){
        viewModel.getUserNoContactState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    contactsAdapter.submitList(dataState.data)
                }
                is DataState.Error -> {
                    activity?.showToast(getString(R.string.error_something_went_wrong))
                }
                else -> Unit
            }
        })

        viewModel.getUserDataInObjectState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    mUser = dataState.data
                }
                is DataState.Error -> {
                    activity?.showToast(getString(R.string.error_something_went_wrong))
                }
                else -> Unit
            }
        })
    }

    private fun initRecyclerView() = binding.rvNewContacts.apply {
        adapter = contactsAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initListeners() {
        contactsAdapter.setItemClickListener {
            showAlert(getString(R.string.message_add_contact), it.userId)
        }
    }

    // TO SHOW ALERT MESSAGE.
    private fun showAlert(message: String, userId: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("")
        builder.setMessage(message)
        builder.setPositiveButton(R.string.button_OK) { _, _ ->
            mUser.contacts = mUser.contacts + userId
            viewModel.saveUser(mUser)
            dismiss()
        }
        builder.setNegativeButton(this.getString(R.string.button_CANCEL), null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}