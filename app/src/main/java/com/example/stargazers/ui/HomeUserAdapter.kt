package com.example.stargazers.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stargazers.R
import com.example.stargazers.models.User

class HomeUserAdapter  :
    RecyclerView.Adapter<HomeUserAdapter.HomeUserHolder>() {

    private var users: ArrayList<User> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeUserHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.home_user_list_row, parent, false)

        return HomeUserHolder(view)
    }

    override fun onBindViewHolder(holder: HomeUserHolder, position: Int) {
        val currentUser: User = users[position]

        holder.textViewUsername.text = currentUser.name
    }

    inner class HomeUserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUsername: TextView = itemView.findViewById(R.id.textview_username)
    }

    override fun getItemCount(): Int {
        return users.size as Int
    }

    fun addUsers(users: ArrayList<User>) {
        val prevTreatmentsCount = this.users.size
        this.users.addAll(users)
        notifyItemRangeInserted(prevTreatmentsCount + 1, users.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNewUsers(users: ArrayList<User>) {
        this.users = users
        //It's good to use notifyDataSetChangd cause the whole set has been changed
        notifyDataSetChanged()
    }
}