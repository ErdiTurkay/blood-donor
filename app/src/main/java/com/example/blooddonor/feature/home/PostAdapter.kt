package com.example.blooddonor.feature.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blooddonor.R
import com.example.blooddonor.data.model.Post
import com.example.blooddonor.data.model.age
import com.example.blooddonor.databinding.ItemPostBinding
import com.example.blooddonor.utils.convertToLocalDateTime
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class BloodAdAdapter(
    var listener: PostClickListener,
) : RecyclerView.Adapter<BloodAdAdapter.ViewHolder>() {
    private var postList = listOf<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        postList[position].run {
            holder.fullName.text = user.name.plus(" ").plus(user.surname)
            holder.bloodGroup.text = bloodType
            holder.age.text = user.age().toString()

            val postCreated = createdAt.convertToLocalDateTime()
            val daysBetween = ChronoUnit.DAYS.between(postCreated, LocalDateTime.now())

            holder.date.text = if (daysBetween > 0) {
                holder.itemView.context.getString(R.string.x_days_ago, daysBetween)
            } else {
                holder.itemView.context.getString(R.string.today)
            }
        }

        Glide.with(holder.itemView.context)
            .load(postList[position].user)
            .placeholder(R.drawable.person_placeholder)
            .fitCenter()
            .into(holder.image)
    }

    override fun getItemCount() = postList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setBloodAdList(bloodAds: List<Post>) {
        postList = bloodAds
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        val fullName = binding.fullName
        val bloodGroup = binding.bloodGroup
        val age = binding.age
        val date = binding.date
        val root = binding.root
        val image = binding.image

        init {
            root.setOnClickListener {
                listener.postOnClick(postList[adapterPosition])
            }
        }
    }
}

interface PostClickListener {
    fun postOnClick(post: Post)
}
