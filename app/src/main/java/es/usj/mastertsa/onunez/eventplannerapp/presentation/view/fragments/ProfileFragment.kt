package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.FragmentProfileBinding
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.LoginViewModel
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.ProfileViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.*
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.DEFAULT_PROFILE_IMAGE
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.EXTRAS_USER
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.INFO_NOT_SET
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_ID
import es.usj.mastertsa.onunez.eventplannerapp.utils.StorageUtils.USER_IMAGE
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.IOException
import kotlinx.coroutines.*

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var mSelectedImageURI: Uri? = null
    private var mProfileImageURL: String = DEFAULT_PROFILE_IMAGE
    private var isImageSelected: Boolean = false
    private var comesFromExtras: Boolean = false

    private var contact: User = User()
    private var mUser: User = User()

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        initObservers()
        viewModel.getUserInObjectData(USER_LOGGED_IN_ID)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contact = arguments?.getParcelable<User>(EXTRAS_USER)?: User()

//        initObservers()
        initListeners()
        initView()
    }

    private fun initView(){
        if (contact.name != INFO_NOT_SET){
            binding.tvEmail.text = contact.email
            binding.etName.setText(contact.name)
            binding.etLastname.setText(contact.lastname)
            binding.etPhone.setText(contact.phoneNumber)
            mProfileImageURL = contact.profileImage
            isImageSelected = true
            comesFromExtras = true
            binding.ivProfilePicture.load(contact.profileImage)
            binding.btnEdit.visibility = View.GONE
            binding.etName.isEnabled = false
            binding.etLastname.isEnabled = false
            binding.etPhone.isEnabled = false
            binding.ivProfilePicture.isEnabled = false
        }
        else {
//            viewModel.getUserInObjectData(USER_LOGGED_IN_ID)

            activity?.showToast("User: " + mUser.name)

            if (mUser.name != INFO_NOT_SET) {
                binding.tvEmail.text = mUser.email
                binding.etName.setText(mUser.name)
                binding.etLastname.setText(mUser.lastname)
                binding.etPhone.setText(mUser.phoneNumber)
                mProfileImageURL = mUser.profileImage
                isImageSelected = true
                comesFromExtras = true
                binding.ivProfilePicture.load(mUser.profileImage)
            }
        }
    }

    private fun initObservers(){
        viewModel.saveUserState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideProgressDialog()
                    activity?.onBackPressed()
                    activity?.showToast(getString(R.string.everything_correctly_saved))
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    activity?.showToast(getString(R.string.error_something_went_wrong))
                }
                else -> Unit
            }
        })

        viewModel.getUserDataInObjectState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideProgressDialog()
                    mUser = dataState.data

//                    if (mUser.name != INFO_NOT_SET) {
//                        binding.tvEmail.text = mUser.email
//                        binding.etName.setText(mUser.name)
//                        binding.etLastname.setText(mUser.lastname)
//                        binding.etPhone.setText(mUser.phoneNumber)
//                        mProfileImageURL = mUser.profileImage
//                        isImageSelected = true
//                        binding.ivProfilePicture.load(mUser.profileImage)
//                    }
                    activity?.showToast("User Observer: " + mUser.name)
//                    Log.d(TAG, "User Fragment: " + mUser.name)
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    activity?.showToast(getString(R.string.error_something_went_wrong))
                }
                else -> Unit
            }
        })
    }

    private fun initListeners(){
        binding.ivProfilePicture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                StorageUtils.showImageChooser(this)
            } else{
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), StorageUtils.READ_STORAGE_PERMISSION_CODE)
            }
        }

        binding.btnEdit.setOnClickListener {
            if (isAllDataSet()){
                if (!isImageSelected){
                    showProgressBar()
                    viewModel.saveUser(User(
                        userId = USER_LOGGED_IN_ID,
                        name = binding.etName.text.toString(),
                        lastname = binding.etLastname.text.toString(),
                        phoneNumber = binding.etPhone.text.toString(),
                        userType = false
                    ))
                    hideKeyboard()
                }else{
                    showProgressBar()
                    if (comesFromExtras){
                        viewModel.saveUser(User(
                            userId = USER_LOGGED_IN_ID,
                            name = binding.etName.text.toString(),
                            lastname = binding.etLastname.text.toString(),
                            phoneNumber = binding.etPhone.text.toString(),
                            profileImage = mProfileImageURL,
                            userType = false
                        ))
                    } else {
                        viewModel.saveProfileImage(requireActivity(), mSelectedImageURI, USER_IMAGE, this)
                    }
                }
            } else {
                activity?.showToast("Debe llenar mínimo los datos de Nombre y Teléfono para continuar")
            }
        }
    }
    private fun isAllDataSet(): Boolean {
        return !binding.etName.text.isNullOrEmpty() && !binding.etPhone.text.isNullOrEmpty()
    }

    fun uploadImageSuccess(imageURL: String){
        hideProgressDialog()
        mProfileImageURL = imageURL

        viewModel.saveUser(User(
            userId = USER_LOGGED_IN_ID,
            name = binding.etName.text.toString(),
            lastname = binding.etLastname.text.toString(),
            phoneNumber = binding.etPhone.text.toString(),
            profileImage = mProfileImageURL,
            userType = false
        ))
        hideKeyboard()
        activity?.onBackPressed()
    }

    private fun hideProgressDialog() {
        binding.btnEdit.isEnabled = true
    }

    private fun showProgressBar() {
        binding.btnEdit.isEnabled = false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            StorageUtils.showImageChooser(this)
        }else{
            activity?.showToast(getString(R.string.read_storage_permission_denied))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == StorageUtils.PICK_IMAGE_REQUEST_CODE){
                if (data != null){
                    mSelectedImageURI=data.data!!
                    isImageSelected = true
                    try {
                        binding.ivProfilePicture.loadURI(mSelectedImageURI!!)
                    }catch (e: IOException){
                        e.printStackTrace()
                        activity?.showToast(getString(R.string.error_something_went_wrong))
                    }
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Log.e("Request cancelled", "Image selection cancelled")
            }
        }
    }
}