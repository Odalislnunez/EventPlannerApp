package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments.public_events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentPublicEventsBinding

class PublicEventsFragment : Fragment() {

    private var _binding: FragmentPublicEventsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val public_eventsViewModel =
            ViewModelProvider(this).get(PublicEventsViewModel::class.java)

        _binding = FragmentPublicEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}