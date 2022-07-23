package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments.invitations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentInvitationsBinding

class InvitationsFragment : Fragment() {

    private var _binding: FragmentInvitationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val invitationsViewModel =
            ViewModelProvider(this).get(InvitationsViewModel::class.java)

        _binding = FragmentInvitationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        invitationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}