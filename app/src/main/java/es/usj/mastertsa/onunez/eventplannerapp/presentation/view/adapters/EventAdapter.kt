package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.presentation.view.adapters.base.EventBaseAdapter
import javax.inject.Inject

class EventAdapter @Inject constructor(

) : EventBaseAdapter(R.layout.item_event) {

    override val differ = AsyncListDiffer(this, diffCallback)
    fun submitList(list : List<Event>) = differ.submitList(list)

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        holder.render(event)

        holder.itemView.apply {

            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(event)
                }
            }
        }
    }
}