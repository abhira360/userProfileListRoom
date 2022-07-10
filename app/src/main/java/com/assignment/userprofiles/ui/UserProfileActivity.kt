package com.assignment.userprofiles.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.assignment.userprofiles.ApplicationConstants
import com.assignment.userprofiles.R
import com.assignment.userprofiles.database.UserProfile
import com.assignment.userprofiles.databinding.ActivityUserProfileBinding
import com.assignment.userprofiles.viewmodel.UserProfileViewModel
import com.bumptech.glide.Glide

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    private lateinit var viewModel: UserProfileViewModel

    var imageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]

        val userProfile = intent.getSerializableExtra(ApplicationConstants.USER) as UserProfile?

        userProfile?.let {

            setField(userProfile)

            imageUri = userProfile.imageUrl
            binding.btnSubmit.visibility = View.GONE
            binding.btnUpdate.visibility = View.VISIBLE
            binding.btnDelete.visibility = View.VISIBLE

            binding.btnUpdate.setOnClickListener {
                updateUser(userProfile)
            }

            binding.btnDelete.setOnClickListener {
                viewModel.deleteUser(userProfile)
                finish()
            }


        }

        binding.btnChoose.setOnClickListener { openDialog()  }

        binding.btnSubmit.setOnClickListener { submitProfile() }




    }

    private fun submitProfile() {

        if (binding.etName.text?.trim().toString().isNotEmpty() && !(imageUri.isNullOrEmpty())) {

            viewModel.submitUser(
                UserProfile(
                    0,
                    binding.etName.text?.trim().toString(),
                    binding.etPhone.text?.trim().toString(),
                    binding.etEmail.text?.trim().toString(),
                    imageUri!!
                )
            )
            finish()
        }
        else {
            Toast.makeText(this,getString(R.string.empty_fields), Toast.LENGTH_LONG).show()
        }

    }


    private fun updateUser(userProfile: UserProfile) {

        if (binding.etName.text?.trim().toString().isNotEmpty() && !(imageUri.isNullOrEmpty())) {

            viewModel.updateUser(
                UserProfile(
                    userProfile.id,
                    binding.etName.text?.trim().toString(),
                    binding.etPhone.text?.trim().toString(),
                    binding.etEmail.text?.trim().toString(),
                    imageUri!!
                )
            )
            finish()
        }
        else {
            Toast.makeText(this,getString(R.string.empty_fields), Toast.LENGTH_LONG).show()
        }
    }

    private fun setField(userProfile: UserProfile) {

        binding.etName.setText(userProfile.name)
        binding.etPhone.setText(userProfile.phone)
        binding.etEmail.setText(userProfile.email)
        if(userProfile.imageUrl.isNotEmpty()) {
            Glide.with(this).load(userProfile.imageUrl).into(binding.ivPhoto)
        }
    }

    private fun openDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(getString(R.string.select))
        val pictureDialogItems = arrayOf(getString(R.string.gallery), getString(R.string.camera))
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> openGallery()
                1 -> openCamera()
            }
        }
        pictureDialog.show()

    }

    private fun openCamera() {
        val openCamera = Intent(this, CameraActivity::class.java)
        getCameraUri.launch(openCamera)
    }

    private val getCameraUri = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data != null) {
                imageUri = it.data!!.extras?.get(ApplicationConstants.CAMERA).toString()
                Glide.with(this).load(imageUri).into(binding.ivPhoto)

            }

        }
    }

    private fun openGallery() {

        if (allPermissionsGranted()) {
            choosePhotoFromGallary()
        }
        else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 11
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }



    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        getResult.launch(galleryIntent)

    }
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data != null) {
                imageUri = it.data!!.dataString.toString()
                Glide.with(this).load(imageUri).into(binding.ivPhoto)

            }
        }
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                choosePhotoFromGallary()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


}