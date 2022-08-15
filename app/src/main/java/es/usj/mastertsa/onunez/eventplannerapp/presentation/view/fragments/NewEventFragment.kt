package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.EventsViewModel
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.NewEventViewModel

class NewEventFragment : Fragment() {

    val viewModel: EventsViewModel by viewModels()

    companion object {
        fun newInstance() = NewEventFragment()
    }

//    private lateinit var viewModel: EventsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_event, container, false)
    }

}