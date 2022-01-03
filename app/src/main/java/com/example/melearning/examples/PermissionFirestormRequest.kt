package com.example.melearning.examples

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat

@Suppress("SameParameterValue", "unused")
class PermissionFirestormRequest {
    companion object {
        private val EXTERNAL_PERMS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        private const val EXTERNAL_REQUEST: Int = 138
    }

    private fun hasPermission(perm: String, context: Context): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(context, perm)
    }

    private fun canAccessExternalSd(context: Context): Boolean {
        return hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, context)
    }

    fun requestForPermission(activity: AppCompatActivity): Boolean {
        var isPermissionOn = true
        val version = Build.VERSION.SDK_INT
        if (version >= 23) {
            if (!canAccessExternalSd(activity)) {
                isPermissionOn = false

                requestPermissions(activity, EXTERNAL_PERMS, EXTERNAL_REQUEST)
            }
        }
        return isPermissionOn
    }
}