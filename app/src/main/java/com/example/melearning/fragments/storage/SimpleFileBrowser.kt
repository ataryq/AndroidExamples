package com.example.melearning.fragments.storage

import androidx.fragment.app.Fragment
import com.anggrayudi.storage.SimpleStorageHelper
import com.anggrayudi.storage.file.getAbsolutePath

class SimpleFileBrowser(private val fragment: Fragment) {
    private val storageHelper = SimpleStorageHelper(fragment)

    fun selectFile(callback: (absolutePath: String) -> Unit) {
        storageHelper.onFileSelected = { _, files ->
            var file = files.firstOrNull()
            if(file != null) {
                println(file.getAbsolutePath(fragment.requireContext()))

                while(file != null) {
                    println(file.name)
                    file = file.parentFile
                }
            }
        }
        storageHelper.openFilePicker()
    }

}