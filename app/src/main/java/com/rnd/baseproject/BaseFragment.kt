package com.rnd.baseproject


import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.rnd.baseproject.tools.Permission


/**
 * Created by Mona on 07-03-2023.
 */
open class BaseFragment : Fragment() {


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

}