package com.example.blooddonor.feature.home

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.blooddonor.R
import com.example.blooddonor.data.model.Post
import com.example.blooddonor.databinding.ItemPostBinding
import com.example.blooddonor.utils.convertToLocalDateTime
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class BloodAdAdapter : RecyclerView.Adapter<BloodAdAdapter.ViewHolder>() {
    private var bloodAdList = listOf<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bloodAdList[position].run {
            val dateTime = user.dateOfBirth.convertToLocalDateTime()
            val age = Calendar.getInstance().get(Calendar.YEAR) - dateTime.year

            holder.fullName.text = user.name.plus(" ").plus(user.surname)
            holder.bloodGroup.text = bloodType
            holder.age.text = age.toString()

            val postCreated = createdAt.convertToLocalDateTime()
            val daysBetween = ChronoUnit.DAYS.between(postCreated, LocalDateTime.now())

            holder.date.text = if (daysBetween > 0) {
                holder.itemView.context.getString(R.string.x_days_ago, daysBetween)
            } else {
                holder.itemView.context.getString(R.string.today)
            }
        }

        /*Glide.with(holder.itemView.context)
            .load(bloodAdList[position].user)
            .placeholder(R.drawable.celalsengor)
            .fitCenter()
            .into(holder.patientImage)*/
    }

    override fun getItemCount() = bloodAdList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setBloodAdList(bloodAds: List<Post>) {
        bloodAdList = bloodAds
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        val fullName = binding.fullName
        val bloodGroup = binding.bloodGroup
        val age = binding.age
        val date = binding.date
    }
}
