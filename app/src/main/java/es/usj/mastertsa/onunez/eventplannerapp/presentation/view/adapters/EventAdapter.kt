package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event

class EventAdapter(){
//class EventAdapter(private val mContext: Context?): ListAdapter<Event, EventAdapter.HomeViewHolder>(PlacesDiffUtilCallback) {
//    inner class HomeViewHolder(val binding: ItemPlaceBinding): RecyclerView.ViewHolder(binding.root)

//    public override fun getItem(position: Int): Place {
//        return super.getItem(position)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
//        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return HomeViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
//        val place = getItem(position)
//        var favorite = place.favorite == "true"
//
//        holder.binding.tvNamePlace.text = place.name
//        holder.binding.tvLocationPlace.text = place.location
//
//        //FOR MANY IMAGES
////        var image: String? = place.images?.get(0)?.let {
////            it.split(",")[0].replace("[","").replace("\\","").replace("\"","")
////        }
//
//        var image: String? = place.images?.let {
//            it.split(",")[0]
//                .replace("[","")
//                .replace("\\","")
//                .replace("\"","")
//        }
//        if (mContext != null) {
//            Glide.with(mContext).load(image).into(holder.binding.ivPlace)
//        }
//        holder.binding.rBPlace.rating = place.rating!!.toFloat()
//        holder.binding.btnFavorite.setImageResource(setFavoriteIcon(favorite))
//        holder.binding.btnFavorite.setOnClickListener {
//            holder.binding.btnFavorite.setImageResource(setFavoriteIcon(!favorite))
//            favorite = !favorite
//            place.favorite = if(place.favorite == "true") "false" else "true"
//        }
//
//        holder.itemView.setOnClickListener {
//            val intent = Intent(mContext, PlaceActivity::class.java)
//            intent.putExtra("place", place)
//            mContext!!.startActivity(intent)
//        }
//    }
//
//    private fun setFavoriteIcon(favorite: Boolean): Int {
//        return if(favorite)
//            R.drawable.ic_favorite_red_24dp
//        else
//            R.drawable.ic_favorite_border_red_24dp
//    }
}

object PlacesDiffUtilCallback: DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
//        return oldItem.code == newItem.code
        return false
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
//        return oldItem.code == newItem.code
        return false
    }
}