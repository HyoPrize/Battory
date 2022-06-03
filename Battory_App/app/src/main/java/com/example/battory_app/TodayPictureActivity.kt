package com.example.battory_app

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.example.battory_app.databinding.PageTodayPictureBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class TodayPictureActivity : AppCompatActivity() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: TodayPictureActivity
        fun ApplicationContext() : Context {
            return instance.applicationContext
        }
    }

    private var mBinding: PageTodayPictureBinding? = null
    private val binding get() = mBinding!!

    private var mToday = 0
    private var mSelectedChallengeIndex = 0
    private var mIsAdd = false

    // Permisisons
    private val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        //Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val PERMISSIONS_REQUEST = 100

    // Request Code
    private val CAPTURE = 100
    private val SAVE = 200

    // 원본 url
    private var photoUri: Uri? = null

    private var mPhotoSaved = false
    private var mTodayPictureName = ""

    // 사진이 들어간 상태로 들어오는 경우는 없음
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = PageTodayPictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mToday = intent.getIntExtra("today", 0)
        mSelectedChallengeIndex = intent.getIntExtra("selectedChallengeIndex", 0)
        binding.todayPictureName.text = "오늘의 사진 (%d 일차)".format(mToday)
        mTodayPictureName = mSelectedChallengeIndex.toString() + "_" + mToday.toString()

        onAddPicture()

        // 사진 클릭
        binding.todayPicturePreviewImage.setOnClickListener {
            checkPermissions(PERMISSIONS, PERMISSIONS_REQUEST)
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile = File(
                File("${filesDir}/image").apply{
                    if(!this.exists()){
                        this.mkdirs()
                    }
                },
                "${mTodayPictureName}.jpg"
            )
            photoUri = FileProvider.getUriForFile(
                this,
                "com.example.battory_app.fileprovider",
                photoFile
            )
            takePictureIntent.resolveActivity(packageManager)?.also{
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePictureIntent, CAPTURE)
            }
        }

        // 사진 찍기 클릭
        binding.todayPictureCapture.setOnClickListener {
            checkPermissions(PERMISSIONS, PERMISSIONS_REQUEST)
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile = File(
                File("${filesDir}/image").apply{
                    if(!this.exists()){
                        this.mkdirs()
                    }
                },
                "${mTodayPictureName}.jpg"
            )
            photoUri = FileProvider.getUriForFile(
                this,
                "com.example.battory_app.fileprovider",
                photoFile
            )
            takePictureIntent.resolveActivity(packageManager)?.also{
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePictureIntent, CAPTURE)
            }
        }

        // 다시 찍기 클릭
        binding.todayPictureReCapture.setOnClickListener {
            val imageFile = File(filesDir.toString() + "/image/" + mTodayPictureName)
            imageFile.delete()

            photoUri = null
            mIsAdd = false
            onAddPicture()
        }

        // 사진 저장 클릭
        binding.todayPictureSave.setOnClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            intent.setType("image/jpg")
            intent.putExtra(Intent.EXTRA_TITLE, ".jpg")
            startActivityForResult(intent, SAVE)
        }

        // 올리기 클릭
        binding.todayPictureUpload.setOnClickListener {
            // ChallengeList.json -> LastAdd 오늘 날짜로 업데이트
            // ChallengeActivity -> doneDay을 +1하고 업데이트
            photoUri = null
            ChallengeActivity.instance.mDoneDay++
            ChallengeActivity.instance.mIsAdd = true

            val now = System.currentTimeMillis()
            val date = Date(now)
            val sdf = SimpleDateFormat("yyyy.MM.dd")
            val today: String = sdf.format(date)

            MainActivity.instance.updateChallengeListJson(ChallengeActivity.instance.mDoneDay, today)
            ChallengeActivity.instance.updateActivity()
            finish()
        }

        // 취소 클릭
        binding.todayPictureCancel.setOnClickListener {
            val imageFile = File(filesDir.toString() + "/image/" + mTodayPictureName + ".jpg")
            imageFile.delete()

            photoUri = null
            finish()
        }
    }

    fun onAddPicture() {
        if(mIsAdd) {
            binding.todayPictureBefore.visibility = View.GONE
            binding.todayPictureAfter.visibility = View.VISIBLE
        } else {
            binding.todayPicturePreviewImage.setImageDrawable(ContextCompat.getDrawable(applicationContext, android.R.drawable.ic_input_add))
            binding.todayPictureBefore.visibility = View.VISIBLE
            binding.todayPictureAfter.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode) {
                CAPTURE -> {
                    val imageBitmap = photoUri?.let { ImageDecoder.createSource(this.contentResolver, it) }
                    val bitmap = imageBitmap?.let { ImageDecoder.decodeBitmap(it) }
                    if (bitmap != null) {
                        val bitmapNotNull = bitmap.copy(Bitmap.Config.ARGB_8888,true)
                        val resizedBitmap = Bitmap.createScaledBitmap(bitmapNotNull, 950, 950, true)
                        binding.todayPicturePreviewImage.setImageBitmap(resizedBitmap)
                    }
                    mIsAdd = true
                    onAddPicture()
                }
                SAVE -> {
                    val savedUri = data?.data;
                    val pfd = savedUri?.let { contentResolver.openFileDescriptor(it, "w") }
                    val fileOutputStream = FileOutputStream(pfd?.fileDescriptor)
                    val bitmap = binding.todayPicturePreviewImage.drawable.toBitmap(950, 950, null)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                    fileOutputStream.close()

                    Toast.makeText(this , savedUri.toString(), Toast.LENGTH_LONG).show()
                    Log.d("saved", savedUri.toString())

                }
            }
        }
    }

    private fun checkPermissions(permissions: Array<String>, permissionsRequest: Int): Boolean {
        val permissionList : MutableList<String> = mutableListOf()
        for(permission in permissions){
            val result = ContextCompat.checkSelfPermission(this, permission)
            if(result != PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission)
            }
        }
        if(permissionList.isNotEmpty()){
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), PERMISSIONS_REQUEST)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(result in grantResults){
            if(result != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "권한 승인 부탁드립니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}