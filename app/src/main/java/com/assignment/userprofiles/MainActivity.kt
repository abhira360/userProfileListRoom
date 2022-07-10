package com.assignment.userprofiles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.userprofiles.adapters.UserProfileListAdapter
import com.assignment.userprofiles.database.UserProfile
import com.assignment.userprofiles.databinding.ActivityMainBinding
import com.assignment.userprofiles.ui.UserProfileActivity
import com.assignment.userprofiles.viewmodel.UserProfileViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]

        binding.btnNew.setOnClickListener { userProfile(null) }

        val rc = binding.recyclerView
        rc.layoutManager = LinearLayoutManager(this)
        val userProfileAdapter = UserProfileListAdapter(this){ userProfile(it) }
        rc.adapter = userProfileAdapter

        lifecycle.coroutineScope.launch {
            viewModel.getAllUser().collect {
                userProfileAdapter.submitList(it)
            }
        }

    }
    private fun userProfile(userProfile: UserProfile?) {
        val userProfileActivity = Intent(this, UserProfileActivity::class.java)
        userProfileActivity.putExtra(ApplicationConstants.USER,userProfile)
        startActivity(userProfileActivity)
    }



}