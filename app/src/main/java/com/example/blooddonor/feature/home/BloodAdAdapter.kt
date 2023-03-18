package com.example.blooddonor.feature.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blooddonor.R
import com.example.blooddonor.data.model.BloodAd
import com.example.blooddonor.databinding.ItemBloodAdBinding

class BloodAdAdapter : RecyclerView.Adapter<BloodAdAdapter.ViewHolder>() {
    private var bloodAdList = listOf<BloodAd>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemBloodAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bloodAdList[position].run {
            holder.patientName.text = patientName
            holder.patientAge.text = patientAge.toString()
            holder.patientBloodGroup.text = patientBloodGroup
        }

        Glide.with(holder.itemView.context)
            .load(bloodAdList[position].patientImage)
            .placeholder(R.drawable.celalsengor)
            .fitCenter()
            .into(holder.patientImage)
    }

    override fun getItemCount() = bloodAdList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setBloodAdList(bloodAds: List<BloodAd>) {
        bloodAdList = bloodAds
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemBloodAdBinding) : RecyclerView.ViewHolder(binding.root) {
        val patientName = binding.patientName
        val patientAge = binding.patientAge
        val patientBloodGroup = binding.patientBloodGroup
        val patientImage = binding.patientImage
    }
}
