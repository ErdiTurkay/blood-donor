package com.erdi.blooddonor.feature.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erdi.blooddonor.R
import com.erdi.blooddonor.data.model.Post
import com.erdi.blooddonor.data.model.User
import com.erdi.blooddonor.databinding.ItemPostBinding
import com.erdi.blooddonor.utils.convertToLocalDateTime
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject

class BloodAdAdapter @Inject constructor(
    var listener: PostClickListener,
    val currentUser: User
) : RecyclerView.Adapter<BloodAdAdapter.ViewHolder>() {
    private var postList = listOf<Post>()

    private var isSwiping = false
    private var initialX = 0f
    private var cardViewOriginalX = 0f
    private lateinit var deleteImageView: ImageView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        postList[position].run {
            holder.fullName.text = patientName.plus(" ").plus(patientSurname)
            holder.bloodGroup.text = patientBloodType
            holder.age.text = patientAge.toString()
            holder.message.text = message

            val postCreated = createdAt.convertToLocalDateTime()

            val truncatedDateTime = postCreated.toLocalDate().atStartOfDay()
            val today = LocalDate.now().atStartOfDay()
            val daysBetween = ChronoUnit.DAYS.between(truncatedDateTime, today).toInt()

            if (currentUser.id == user.id) {
                holder.root.strokeWidth = 3
            }

            holder.date.text = when (daysBetween) {
                0 -> holder.itemView.context.getString(R.string.today)
                1 -> holder.itemView.context.getString(R.string.yesterday)
                else -> holder.itemView.context.getString(R.string.x_days_ago, daysBetween)
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
        postList = bloodAds.reversed()
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        val fullName = binding.fullName
        val bloodGroup = binding.bloodGroup
        val age = binding.age
        val date = binding.date
        val message = binding.message
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
