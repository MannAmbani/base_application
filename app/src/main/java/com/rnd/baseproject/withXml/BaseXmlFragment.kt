package com.rnd.baseproject.withXml

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.rnd.baseproject.R
import com.rnd.baseproject.databinding.ToolbarBinding
import com.rnd.baseproject.tools.Permission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseXmlFragment(layoutResourceId: Int) : Fragment(layoutResourceId), CoroutineScope {
    private lateinit var job: Job
    lateinit var toolbarBinding: ToolbarBinding

    //    lateinit var location: GpsTracker
    var isGPS: Boolean = false

    companion object {
        val TAG = "TAG"
    }

//    protected fun startTrackLocation() {
////        Handler(Looper.getMainLooper()).post {
//        location = GpsTracker(requireContext())
////        }
//    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //mona's code
        try {
            val toolbarView: View? = view.findViewById<ConstraintLayout>(R.id.toolbar)
            toolbarView?.let {
                toolbarBinding = ToolbarBinding.bind(toolbarView)
                toolbarBinding.ivBack.setOnClickListener {
                    onBackClicked()
                }
                toolbarBinding.tvTitle.setOnClickListener {
                    toolbarBinding.ivBack.callOnClick()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    open fun setTitle(title: String) {
        toolbarBinding.tvTitle.text = title
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == GpsUtils.GPS_REQUEST) {
//            isGPS = true; // flag maintain before get location
//
//        }
    }

    //mona's code
    val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            Permission.check(requireContext(), it) {
                onPermissionGranted()
            }
        }

    val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            Permission.check(requireContext(), it) {
                onCameraPermissionGranted()
            }
        }

    //mona's code
    open fun onPermissionGranted() {}
    open fun onCameraPermissionGranted() {}


    //mona's code
    fun requestLocationPermission() {
        Permission.ask(
            requestPermission, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    fun requestCameraPermission() {
        Permission.ask(
            requestCameraPermission, arrayOf(
                android.Manifest.permission.CAMERA,
            )
        )
    }

    fun requestStoragePermission() {
        /**Android 13 or higher no need to ask storage permission*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Permission.ask(
                requestPermission, arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        } else {
            onPermissionGranted()
        }
    }

    //mona's code
//    fun getDeviceLocation() {
//        GpsTracking(requireActivity()) { location ->
//            onLocationGet(location.latitude, location.longitude)
//        }
//    }

    //mona's code
    open fun onLocationGet(lat: Double, long: Double) {}

    //mona's code
    open fun onBackClicked() {
        findNavController().navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        Lifecycle.Event.ON_DESTROY
        job.cancel()
    }
}