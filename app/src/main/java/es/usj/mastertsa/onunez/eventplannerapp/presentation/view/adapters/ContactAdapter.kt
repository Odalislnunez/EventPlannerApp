package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.presentation.view.adapters.base.ContactBaseAdapter
import kotlinx.android.synthetic.main.fragment_new_event.view.*
import javax.inject.Inject

class ContactAdapter @Inject constructor(

) : ContactBaseAdapter(R.layout.item_contact) {

    override val differ = AsyncListDiffer(this, diffCallback)
    fun submitList(list : List<User>) = differ.submitList(list)

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]

        holder.render(contact)

//        holder.itemView.btnCancel.setOnClickListener {
//            onDeleteClickListener?.let { click ->
//                click(contact)
//            }
//        }

        holder.itemView.apply {

            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(contact)
                }
            }
        }
    }
}