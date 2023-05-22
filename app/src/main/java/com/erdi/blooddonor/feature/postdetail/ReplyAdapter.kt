package com.erdi.blooddonor.feature.postdetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erdi.blooddonor.R
import com.erdi.blooddonor.data.model.Reply
import com.erdi.blooddonor.data.model.fullName
import com.erdi.blooddonor.databinding.ItemReplyBinding
import com.erdi.blooddonor.utils.convertToLocalDateTime
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ReplyAdapter() : RecyclerView.Adapter<ReplyAdapter.ViewHolder>() {
    private var replyList = listOf<Reply>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyAdapter.ViewHolder {
        val view = ItemReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReplyAdapter.ViewHolder, position: Int) {
        replyList[position].run {
            holder.fullName.text = from.fullName()
            holder.content.text = content

            val postCreated = date.convertToLocalDateTime()
            val daysBetween = ChronoUnit.DAYS.between(postCreated, LocalDateTime.now()).toInt()

            holder.date.text = when (daysBetween) {
                0 -> holder.itemView.context.getString(R.string.today)
                1 -> holder.itemView.context.getString(R.string.yesterday)
                else -> holder.itemView.context.getString(R.string.x_days_ago, daysBetween)
            }
        }

        Glide.with(holder.itemView.context)
            .load(replyList[position].from)
            .placeholder(R.drawable.person_placeholder)
            .fitCenter()
            .into(holder.image)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setReplyList(replies: List<Reply>) {
        replyList = replies.reversed()
        notifyDataSetChanged()
    }

    override fun getItemCount() = replyList.size

    inner class ViewHolder(binding: ItemReplyBinding) : RecyclerView.ViewHolder(binding.root) {
        val fullName = binding.fullName
        val date = binding.date
        val root = binding.root
        val image = binding.image
        val content = binding.content
    }
}
