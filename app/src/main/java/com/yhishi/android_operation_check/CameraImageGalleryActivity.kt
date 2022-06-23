package com.yhishi.android_operation_check

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
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
                Log.d("hishiii", "camera permission granted")
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
                binding.imageView.setImageURI(cameraUri)

//                MediaScannerConnection.scanFile(
//                    this,
//                    arrayOf(file.absolutePath),
//                    arrayOf("image/jpeg"),
//                ) { path, uri ->
//                    Log.d("hishiii", "scanFile path: $path")
//                    Log.d("hishiii", "scanFile uri: $uri")
//                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera_image_gallery)

        binding.getExternalFilesDirButton.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                createFileOnGetExternalFilesDir()
                takePicture()
            }
        }

        binding.mediaStoreButton.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                createUri()
                takePicture()
            }
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun createFileOnGetExternalFilesDir() {
        // /storage/emulated/0/Android/data/com.yhishi.android_operation_check/files/Pictures/CameraIntent_yyyyMMdd_HHmmss.jpegに保存される
        // アプリ固有のストレージのため、フォトやFilesの画像では確認することができない
        // 参考情報：https://akira-watson.com/android/mediastore-save.html
        // 参考情報：https://developer.android.com/training/data-storage
        val directory: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.d("hishiii", "path: $directory")

        val cameraFile = File(directory, createImageName())

        cameraUri = FileProvider.getUriForFile(
            this,
            "$packageName.fileprovider",
            cameraFile,
        )

        Log.d("hishiii", "cameraFile path: ${cameraFile.path}")
        Log.d("hishiii", "cameraFile absolutePath: ${cameraFile.absolutePath}")

        fieldCameraFile = cameraFile
    }

    private fun createImageName() :String {
        val fileDate = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN).format(Date())
        return String.format("CameraIntent_%s.jpeg", fileDate)
    }

    private fun createUri() {
        val fileName = createImageName()
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
        cameraUri = contentResolver.insert(collection, values)
    }

    private fun takePicture() {
        if (cameraUri != null) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

            resultLauncher.launch(intent)
        }
    }
}
