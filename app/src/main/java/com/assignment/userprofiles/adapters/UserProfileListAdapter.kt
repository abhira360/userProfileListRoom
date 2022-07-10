package com.assignment.userprofiles.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.assignment.userprofiles.R
import com.assignment.userprofiles.database.UserProfile
import com.assignment.userprofiles.databinding.ItemUserBinding
import com.bumptech.glide.Glide


class UserProfileListAdapter(val context: Context, private val onItemClicked:(UserProfile)-> Unit):
    ListAdapter<UserProfile, UserProfileListAdapter.UserProfileViewHolder>(UserProfileDiffCallback)
{

    class UserProfileViewHolder(private var binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(userProfile: UserProfile, context: Context) {

            if (userProfile.imageUrl.isNotEmpty()) {
                Glide.with(context).load(userProfile.imageUrl).into(binding.ivProfilePhoto)
            }

            (context.resources.getString(R.string.name)+ " " + userProfile.name + context.resources.getString(R.string.new_line) +
                    context.resources.getString(R.string.phone)+ " "  + userProfile.phone+ context.resources.getString(R.string.new_line)+
                    context.resources.getString(R.string.email)+" " + userProfile.email).also { binding.tvName.text = it }


        }

    }

    companion object {

        private val UserProfileDiffCallback = object : DiffUtil.ItemCallback<UserProfile>() {
            override fun areItemsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
                return oldItem == newItem
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileViewHolder {

        val viewHolder = UserProfileViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))

        viewHolder.itemView.setOnClickListener {
            val position =viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: UserProfileViewHolder, position: Int) {
        holder.bind(getItem(position), context)
    }

}