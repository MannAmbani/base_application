package com.rnd.baseproject.ui.authpage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rnd.baseproject.BaseFragment
import com.rnd.baseproject.R
import com.rnd.baseproject.activity.ImageCropActivity
import com.rnd.baseproject.activity.PicModeSelectDialogFragment
import com.rnd.baseproject.retrofit_api.getImageMultipartFile
import com.rnd.baseproject.retrofit_api.getParamMultipart
import com.rnd.baseproject.tools.ConstValue
import com.rnd.baseproject.tools.getUserData
import com.rnd.baseproject.tools.handleResponse
import com.rnd.baseproject.tools.saveUserData
import com.rnd.baseproject.tools.toast
import com.rnd.baseproject.ui.theme.BaseProjectTheme
import com.rnd.baseproject.ui.widgets.SmsCodeView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : BaseFragment(), PicModeSelectDialogFragment.IPicModeSelectListener {

    private val viewModel by viewModels<AuthViewModel>()
    var aadharCard: MutableState<String> = mutableStateOf("")
    var isErrorEnableAadharCard: MutableState<Boolean> = mutableStateOf(false)
    var aadharCardErrorMsg: MutableState<String> = mutableStateOf("")
    private val otpValue: MutableState<String> = mutableStateOf("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.createAccountResponse.observe(viewLifecycleOwner) {
            requireContext().handleResponse(true, it) {
                if (it.data != null) {
                    it.message?.let { message ->
                        toast(message)
                    }
                    it.data.data.let { user ->
                        saveUserData(user)
                    }
//                    findNavController().navigate(R.id.action_createAccountFragment_to_dashboardFragment)
                }
            }
        }

        viewModel.otpVerifyResponse.observe(viewLifecycleOwner) {
            requireContext().handleResponse(true, it) {
                if (it.data?.data != null) {
                    saveUserData(it.data.data)
                }

            }
        }

        return ComposeView(requireContext()).apply {
            setContent {
                BaseProjectTheme {
                    AuthMainContent()
                }
            }
        }
    }

    override fun onCameraPermissionGranted() {
        super.onCameraPermissionGranted()
        requestStoragePermission()
    }

    override fun onPermissionGranted() {
        super.onPermissionGranted()
        showAddProfilePicDialog(0)
    }


    private fun showAddProfilePicDialog(pos: Int) {
        val dialogFragment = PicModeSelectDialogFragment()
        dialogFragment.setiPicModeSelectListener(this, pos)
        dialogFragment.show(
            childFragmentManager, "picModeSelector"
        )
    }

    override fun onPicModeSelected(mode: String?, position: Int) {
        val action: String = if (mode.equals(
                PicModeSelectDialogFragment.PicModes.CAMERA, ignoreCase = true
            )
        ) PicModeSelectDialogFragment.IntentExtras.ACTION_CAMERA else PicModeSelectDialogFragment.IntentExtras.ACTION_GALLERY
        actionProfilePic(action)
    }

    private fun actionProfilePic(action: String?) {
        val intent = Intent(requireContext(), ImageCropActivity::class.java)
        intent.putExtra("ACTION", action)
        //curtain for full image / remove curtain and you will able to get crop image
        intent.putExtra("tag", "curtain")
        intent.putExtra(PicModeSelectDialogFragment.PicModes.IS_MULTI_SELECT, false)
        startActivityForResult(intent, ConstValue.REQUEST_CODE_UPDATE_PIC)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            ConstValue.REQUEST_CODE_UPDATE_PIC -> {
                if (data != null) {
                    val arrImage: String? =
                        data.getStringExtra(PicModeSelectDialogFragment.IntentExtras.IMAGE_PATH)
                    if (arrImage != null) {
                        if (arrImage.isNotEmpty()) {
                            aadharCard.value = arrImage
                        }
                    }
                }
            }
        }
    }

    private fun createAccountApiCall(){
        val params: HashMap<String, Any> = HashMap()
        params["user_id"] = getUserData().id

        viewModel.createAccountApiCall(
            getParamMultipart(params), getImageMultipartFile(
                aadharCard.value, "adhar_image"
            )
        )
    }
    private fun otpVerifyApiCall() {
        val params: HashMap<String, Any> = HashMap()
        params["user_id"] = getUserData().id

        viewModel.otpVerifyApiCall(params)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun AuthMainContent() {
        Scaffold {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = aadharCard.value,
                    onValueChange = { value ->
                        aadharCard.value = value
                        isErrorEnableAadharCard.value = false
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.upload_aadhar_card_front_page),
                            fontSize = 12.sp
                        )
                    },
                    textStyle = TextStyle(fontSize = 12.sp),
                    readOnly = true,
                    isError = isErrorEnableAadharCard.value,
                    shape = RoundedCornerShape(20.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.image_upload_leading),
                            contentDescription = "",
                            Modifier.size(24.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(end = 20.dp, start = 20.dp),
                    supportingText = {
                        if (isErrorEnableAadharCard.value) {
                            Text(text = aadharCardErrorMsg.value)
                        }
                    },
                    trailingIcon = {
                        Icon(painter = painterResource(id = R.drawable.image_upload_trailing),
                            contentDescription = "",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    isErrorEnableAadharCard.value = false
                                    requestCameraPermission()
                                })
                    },

                    )
                
                SmsCodeView(smsCodeLength = 6, textStyle = TextStyle(fontSize = 14.sp), smsFulled = {otp ->
                    otpValue.value = otp
                })
                
            }
        }
    }
}