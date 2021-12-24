package com.example.melearning.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import com.example.melearning.databinding.FilePickerFragmentBinding
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

@Suppress("unused")
class FilePickerFragment:
    BaseBindFragment2<FilePickerFragmentBinding>(FilePickerFragmentBinding::inflate) {

    companion object {
        const val FILE_SELECT_CODE = 1
    }

    override fun initViews() {
        binding.pickFileButton.setOnClickListener {
            val intent: Intent
            if (Build.MANUFACTURER.equals("samsung", ignoreCase = true)) {
                intent = Intent("com.sec.android.app.myfiles.PICK_DATA")
                intent.putExtra("CONTENT_TYPE", "*/*")
                intent.addCategory(Intent.CATEGORY_DEFAULT)
            } else {
                val mimeTypes = arrayOf(
                    "image/jpeg",
                    "image/bmp",
                    "image/gif",
                    "image/jpg",
                    "image/png",
                    "application/vnd.android.package-archive"
                )
                intent = Intent(Intent.ACTION_GET_CONTENT) // or ACTION_OPEN_DOCUMENT
                intent.type = "*/*"
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            }
            startActivityForResult(intent, FILE_SELECT_CODE);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                println("FilePickerFragment: onActivityResult: picked file: ${data?.data}")
                val uri = data?.data ?: return
                val bitmap = getBitmapFromUri(uri)
                binding.pickedImage.setImageBitmap(bitmap)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun readFile(uri: Uri): ByteArray {
        val contentResolver = requireContext().applicationContext.contentResolver
        try {
            contentResolver.openFileDescriptor(uri, "r")?.use { it ->

                FileInputStream(it.fileDescriptor)
                FileInputStream(it.fileDescriptor).use {
                    return it.readBytes()
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return ByteArray(0)
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        val contentResolver = requireContext().applicationContext.contentResolver
        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r") ?: return null
        val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
        val image: Bitmap? = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }

}