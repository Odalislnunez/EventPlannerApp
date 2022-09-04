package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.adapters.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import es.usj.mastertsa.onunez.eventplannerapp.databinding.ItemContactBinding
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.utils.load

abstract class ContactBaseAdapter (
    private val layoutId : Int
): RecyclerView.Adapter<ContactBaseAdapter.ContactViewHolder>() {

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = ItemContactBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun render(contact: User) {
            binding.ivUser.load(contact.profileImage)
            binding.tvName.text = contact.name + " " + contact.lastname
            binding.tvEmail.text = contact.email
            binding.tvPhone.text = contact.phoneNumber
        }
    }

    protected val diffCallback = object : DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    protected abstract val differ : AsyncListDiffer<User>

    open var contacts : List<User>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )
    }

    protected var onItemClickListener : ((User) -> Unit)? = null
    protected var onDeleteClickListener : ((User) -> Unit)? = null

    fun setItemClickListener(listener: (User) -> Unit){
        onItemClickListener = listener
    }

    fun setDeleteClickListener(listener: (User) -> Unit){
        onDeleteClickListener = listener
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

}