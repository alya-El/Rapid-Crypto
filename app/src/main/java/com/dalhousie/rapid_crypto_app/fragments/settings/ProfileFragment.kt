package com.dalhousie.rapid_crypto_app.fragments.settings

import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dalhousie.rapid_crypto_app.R
import com.dalhousie.rapid_crypto_app.models.Profile
import com.dalhousie.rapid_crypto_app.utils.DownloadImageFromInternet
import com.dalhousie.rapid_crypto_app.viewmodels.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView


class ProfileFragment : Fragment() {

    private val firebaseUser = FirebaseAuth.getInstance().currentUser

    private val profileViewModel: ProfileViewModel by activityViewModels()
    var imageuri: Uri? = null
    var profileUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = profileViewModel.getUserEmail()
        view.findViewById<TextView>(R.id.email_text).text = email

        view.findViewById<RelativeLayout>(R.id.profile_edit_button).setOnClickListener {
            showImagePicDialog()
        }

        loadAndObserveProfileData()
        handleSaveProfileChanges()
    }

    private fun loadAndObserveProfileData() {
        profileViewModel.loadProfile()
        profileViewModel.profileLiveData.observe(
            viewLifecycleOwner,
            {
                view?.findViewById<EditText>(R.id.name_input)?.setText(it.name)
                view?.findViewById<EditText>(R.id.phone_number_input)?.setText(it.phone)

                // Spinners are set according to position, but database saves countries as a string
                // This gets the country string and finds the corresponding array position
                val pos = activity?.resources?.getStringArray(R.array.countries_array)
                    ?.indexOf(it.country)
                if (pos != null) {
                    view?.findViewById<Spinner>(R.id.country_input)?.setSelection(pos)
                }

                val profileImageView =
                    view?.findViewById<CircleImageView>(R.id.setting_profile_image)
                if (it.image.isNotBlank() && profileImageView != null) {
                    profileUrl = it.image
                    DownloadImageFromInternet(profileImageView).execute(it.image)
                }
            }
        )
    }

    private fun handleSaveProfileChanges() {
        view?.findViewById<Button>(R.id.save_changes_button)?.setOnClickListener {
            val name = view?.findViewById<EditText>(R.id.name_input)?.text.toString()
            val phone = view?.findViewById<EditText>(R.id.phone_number_input)?.text.toString()
            val country = view?.findViewById<Spinner>(R.id.country_input)?.selectedItem.toString()
            val profile = Profile(country, name, phone, image = profileUrl)
            profileViewModel.updateProfile(profile)
            view?.let {
                Snackbar.make(
                    it,
                    "Changes Saved",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showImagePicDialog() {
        val options = arrayOf("Camera", "Gallery")
        val alert: AlertDialog.Builder? = activity?.let { AlertDialog.Builder(it) }
        alert?.setTitle("Pick Image From")
        alert?.setItems(
            options,
            DialogInterface.OnClickListener { dialog, which ->
                if (which == 0) {
                    view?.let {
                        Snackbar.make(
                            it,
                            "Camera",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    pickFromCamera()
                } else if (which == 1) {
                    view?.let {
                        Snackbar.make(
                            it,
                            "Gallery",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    pickFromGallery()
                }
            })

        alert?.show()
    }

    private fun pickFromCamera() {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description")
        imageuri = activity?.getContentResolver()
            ?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        val camerIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri)
        startActivityForResult(camerIntent, 100)
    }

    private fun pickFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                Log.w(ContentValues.TAG, "Image URI: $imageuri")
                imageuri?.let { uploadProfileCoverPhoto(it) }
            } else if (requestCode == 200) {
                imageuri = data?.getData()
                Log.w(ContentValues.TAG, "Image URI: $imageuri")
                imageuri?.let { uploadProfileCoverPhoto(it) }
            }
        }
    }

    private fun uploadProfileCoverPhoto(uri: Uri) {
        val storageReference = FirebaseStorage.getInstance().getReference()
        val storageReference1 = storageReference.child("images/" + firebaseUser?.email.toString() + ".jpg")

        // uploading picture to the firestore
        val uploadTask = storageReference1.putFile(uri)
        uploadTask.addOnFailureListener {
            view?.let {
                Snackbar.make(
                    it,
                    "Photo upload failed",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }.addOnSuccessListener { taskSnapshot ->
            view?.let {
                Snackbar.make(
                    it,
                    "Photo upload successful",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageReference1.downloadUrl
        }.addOnCompleteListener { taskSnapshot ->
            val downloadUri = taskSnapshot.result
            profileUrl = downloadUri.toString()

            view?.findViewById<CircleImageView>(R.id.setting_profile_image)
                ?.let { DownloadImageFromInternet(it).execute(profileUrl) }
        }
    }
}