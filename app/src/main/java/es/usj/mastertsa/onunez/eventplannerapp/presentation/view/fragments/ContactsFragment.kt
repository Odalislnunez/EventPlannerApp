package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentContactsBinding
import es.usj.mastertsa.onunez.eventplannerapp.presentation.view.adapters.ContactAdapter
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.UsersViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_ID
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UsersViewModel by viewModels()

    @Inject
    lateinit var contactsAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
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
        viewModel.getUserContact(USER_LOGGED_IN_ID)
    }

    private fun initObservers(){
        viewModel.getUserContactState.observe(viewLifecycleOwner, Observer { dataState ->
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
    }

    private fun initRecyclerView() = binding.rvContacts.apply {
        adapter = contactsAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initListeners() {
        contactsAdapter.setItemClickListener {
            val bundle = bundleOf(Constants.EXTRAS_USER to it)
            findNavController().navigate(R.id.action_nav_contacts_to_nav_profile, bundle )
        }

        binding.btnAddContact.setOnClickListener {
            val showAddContact = AddContactFragment()
            showAddContact.show((activity as AppCompatActivity).supportFragmentManager, "showAddContact")
        }
    }

}