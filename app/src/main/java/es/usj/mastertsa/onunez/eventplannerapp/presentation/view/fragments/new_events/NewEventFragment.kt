package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments.new_events

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.usj.mastertsa.onunez.eventplannerapp.R

class NewEventFragment : Fragment() {

    companion object {
        fun newInstance() = NewEventFragment()
    }

    private lateinit var viewModel: NewEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_event, container, false)
    }

}