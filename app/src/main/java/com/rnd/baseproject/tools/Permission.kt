package com.rnd.baseproject.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


object Permission {

    const val MULTIPLE_PERMISSION_REQUEST_CODE = 111

    fun checkSelfPermission(activity: Context?, permissions: String): Boolean {

        val result = ContextCompat.checkSelfPermission(
            activity!!, permissions
        )
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            DialogUtils.showAlertDialog(activity,
                "Permission Grant",
                "permission needed. Please allow in App Settings for additional functionality.",
                "Settings",
                View.OnClickListener {
                    DialogUtils.cancel()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
                    activity.startActivity(intent)
                })
            false
        }
    }

    fun showSettingDialog(activity: Context?) {
        DialogUtils.showAlertDialog(activity,
            "Permission Grant",
            "permission needed. Please allow in App Settings for additional functionality.",
            "Settings",
            View.OnClickListener {
                DialogUtils.cancel()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
                intent.data = uri
                activity?.startActivity(intent)
            })
    }

    fun requestMultiplePermission(activity: Activity, permissions: Array<String?>) {
//        val permissionLauncher = registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted ->
//            if (isGranted) {
//                // Do if the permission is granted
//            }
//            else {
//                // Do otherwise
//            }
//        }
//
//        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(
            activity, permissions, MULTIPLE_PERMISSION_REQUEST_CODE
        )
    }

    fun ask(
        requestMultiplePermissions: ActivityResultLauncher<Array<String>>,
        permissions: Array<String>
    ) {
        requestMultiplePermissions.launch(permissions)
    }

    fun check(
        context: Context,
        permissions: Map<String, @JvmSuppressWildcards Boolean>,
        isGranted: () -> Unit
    ) {
        var result = false

        permissions.entries.forEach {
            result = it.value
        }

        if (result) {
            isGranted.invoke()
        } else {
            showSettingDialog(context)
        }
    }
}