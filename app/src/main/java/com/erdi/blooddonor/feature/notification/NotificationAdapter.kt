package com.erdi.blooddonor.feature.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erdi.blooddonor.data.model.NotificationItem
import com.erdi.blooddonor.databinding.ItemNotificationBinding

class NotificationAdapter(
    var listener: NotificationClickListener,
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    private var notificationList = listOf<NotificationItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {
        val view = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        notificationList[position].run {
            holder.title.text = title
            holder.body.text = message
            holder.date.text = date
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNotificationList(notifications: List<NotificationItem>) {
        notificationList = notifications.reversed()
        notifyDataSetChanged()
    }

    override fun getItemCount() = notificationList.size

    inner class ViewHolder(binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.notificationTitle
        val body = binding.notificationBody
        val date = binding.date
        val root = binding.root

        init {
            root.setOnClickListener {
                listener.notificationOnClick(notificationList[adapterPosition].postId)
            }
        }
    }
}

interface NotificationClickListener {
    fun notificationOnClick(postId: String)
}
