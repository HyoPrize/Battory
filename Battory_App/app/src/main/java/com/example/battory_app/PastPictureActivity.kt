package com.example.battory_app

import android.R.attr.bitmap
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.battory_app.databinding.PagePastPictureBinding
import java.io.File


class PastPictureActivity : AppCompatActivity() {
    private var mBinding: PagePastPictureBinding? = null
    private val binding get() = mBinding!!

    private var mSelectedChallengeIndex = -1
    private var mDay = -1
    private var mRed = -1
    private var mGreen = -1
    private var mBlue = -1

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = PagePastPictureBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mSelectedChallengeIndex = intent.getIntExtra("selectedChallengeIndex", 0)
        mDay = intent.getIntExtra("day", 0)
        mRed = intent.getIntExtra("red", 255)
        mGreen = intent.getIntExtra("green", 255)
        mBlue = intent.getIntExtra("blue", 255)

        binding.pastPictureName.text = "%d일차 업로드 사진".format(mDay)

        val photoFile = File(
            File("${filesDir}/image").apply{
                if(!this.exists()){
                    this.mkdirs()
                }
            },
            "${mSelectedChallengeIndex.toString()}_${mDay.toString()}.jpg"
        )
        val photoUri = FileProvider.getUriForFile(
            this,
            "com.example.battory_app.fileprovider",
            photoFile
        )
        Log.d("Uri", photoUri.toString())
        Log.d("photoName", "${mSelectedChallengeIndex.toString()}_${mDay.toString()}.jpg")
        val imageBitmap = photoUri?.let { ImageDecoder.createSource(this.contentResolver, it) }
        val bitmap = imageBitmap?.let { ImageDecoder.decodeBitmap(it) }
        if (bitmap != null) {
            val bitmapNotNull = bitmap.copy(Bitmap.Config.ARGB_8888,true)

            val paint = Paint()
            val colorFilter: ColorFilter = PorterDuffColorFilter(Color.argb(128, mRed,mGreen,mBlue), PorterDuff.Mode.OVERLAY)
            paint.colorFilter = colorFilter

            val canvas = Canvas(bitmapNotNull)
            val src = Rect(0,0, bitmapNotNull.width, bitmapNotNull.height)
            val dest = Rect(0,0, canvas.width, canvas.height)
            canvas.drawBitmap(bitmapNotNull, src, dest, paint)

            binding.pastPicture.setImageBitmap(bitmapNotNull)
        }


        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}