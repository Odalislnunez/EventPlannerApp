package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.EditEventViewModel

class EditEventFragment : Fragment() {

    companion object {
        fun newInstance() = EditEventFragment()
    }

    private lateinit var viewModel: EditEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_event, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditEventViewModel::class.java)
        // TODO: Use the ViewModel
    }

}