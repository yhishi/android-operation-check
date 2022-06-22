package com.yhishi.android_operation_check

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.yhishi.android_operation_check.databinding.ActivityCameraImageGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class CameraImageGalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraImageGalleryBinding
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                takePicture()
            } else {
                // TODO
            }
        }

    private var cameraUri: Uri? = null
    private var fieldCameraFile: File? = null

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {
                fieldCameraFile?.let { file ->
                    binding.imageView.setImageURI(cameraUri)

                    MediaScannerConnection.scanFile(
                        this,
                        arrayOf(file.absolutePath),
                        arrayOf("image/jpeg"),
                    ) { path, uri ->
                        Log.d("hishiii", "scanFile path: $path")
                        Log.d("hishiii", "scanFile uri: $uri")
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera_image_gallery)

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePicture()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun takePicture() {
        val directory: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.d("hishiii", "path: $directory")
        val fileDate = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN).format(Date())
        val fileName = String.format("CameraIntent_%s.jpeg", fileDate)

        val cameraFile = File(directory, fileName)

        cameraUri = FileProvider.getUriForFile(
            this,
            "$packageName.fileprovider",
            cameraFile,
        )

        Log.d("hishiii", "cameraFile path: ${cameraFile.path}")
        Log.d("hishiii", "cameraFile absolutePath: ${cameraFile.absolutePath}")

        if (cameraUri != null) {
            fieldCameraFile = cameraFile
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

            resultLauncher.launch(intent)
        }
    }
}
