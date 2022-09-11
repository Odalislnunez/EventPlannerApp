package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.adapters.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import es.usj.mastertsa.onunez.eventplannerapp.databinding.ItemEventBinding
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import java.text.SimpleDateFormat
import java.util.*

abstract class EventBaseAdapter (
    private val layoutId : Int
): RecyclerView.Adapter<EventBaseAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = ItemEventBinding.bind(itemView)

        @SuppressLint("SimpleDateFormat")
        fun render(event: Event) {
//            val mDate = Date(event.datetime.time)
            binding.tvTitle.text = event.title
            binding.tvPlace.text = event.place
            val mDate: List<String> = event.datetime.split(" ").toList()

            val date = SimpleDateFormat("dd/MM/yyyy").parse(mDate.toString())?.toString() ?: ""
            val time = SimpleDateFormat("HH:mm").parse(mDate.toString())?.toString() ?: ""
            binding.tvDate.text = "$date $time" //SimpleDateFormat("dd-MM-yyyy").parse(mDate.toString())?.toString() ?: ""
        }
    }

    protected val diffCallback = object : DiffUtil.ItemCallback<Event>(){
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.eventId == newItem.eventId
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    protected abstract val differ : AsyncListDiffer<Event>

    open var events : List<Event>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )
    }

    protected var onItemClickListener : ((Event) -> Unit)? = null

    fun setItemClickListener(listener: (Event) -> Unit){
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return events.size
    }

}