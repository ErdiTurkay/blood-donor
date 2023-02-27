package com.example.blooddonor.feature

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blooddonor.R
import com.example.blooddonor.data.model.BloodAd

class BloodAdAdapter: RecyclerView.Adapter<BloodAdAdapter.ViewHolder>() {
    private var bloodAdList = listOf<BloodAd>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BloodAdAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blood_ad, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BloodAdAdapter.ViewHolder, position: Int) {
        holder.patientName.text = bloodAdList[position].patientName
        holder.patientAge.text = bloodAdList[position].patientAge.toString()
        holder.patientBloodGroup.text = bloodAdList[position].patientBloodGroup

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

    inner class ViewHolder(_itemView: View) : RecyclerView.ViewHolder(_itemView) {
        val patientName: TextView = _itemView.findViewById(R.id.patient_name)
        val patientAge: TextView = _itemView.findViewById(R.id.patient_age)
        val patientBloodGroup: TextView = _itemView.findViewById(R.id.patient_blood_group)
        val patientImage: ImageView = _itemView.findViewById(R.id.patient_image)
    }
}