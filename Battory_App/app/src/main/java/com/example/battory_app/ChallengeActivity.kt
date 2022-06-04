package com.example.battory_app

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.battory_app.databinding.ChallengeBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.nio.charset.Charset

class ChallengeActivity : AppCompatActivity() {

    init {
        instance = this
    }

    companion object {
        lateinit var instance: ChallengeActivity
        fun ApplicationContext() : Context {
            return instance.applicationContext
        }
    }

    private var mBinding: ChallengeBinding? = null
    private val binding get() = mBinding!!

    private var jsonString:String = ""
    private var jsonArray = JSONObject()
    private var imageInfo = JSONObject()
    private var imageData = JSONObject()
    private var imagePixelPerDay = JSONObject()

    public var mIsAdd = false
    public var mSelectedChallengeIndex = -1
    public var mDoneDay = 0

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mIsAdd = intent.getBooleanExtra("isAdd", false)
        mSelectedChallengeIndex = intent.getIntExtra("selectedChallengeIndex", -1)
        mDoneDay = intent.getIntExtra("doneDay", -1)

        // Save json 추가시 doneDay 날짜 선택 필요
        jsonString = assets.open("pikachu_info.json").reader().readText()
        jsonArray = JSONObject(jsonString)
        imageInfo = jsonArray.getJSONObject("info")
        imageData = jsonArray.getJSONObject("data")
        imagePixelPerDay = jsonArray.getJSONObject("pixel_per_day")

        // bitmap & canvas 생성(픽셀 그리기)
        updateActivity()

        // 챌린지 관련 세팅
        binding.challengeName.text = "오늘까지 개발 끝내기"

        // 사진 추가하기 클릭
        binding.addPicture.setOnClickListener {
            onClickToday()
        }


        // 상단 옵션바
        binding.challengeBtnHome.setOnClickListener {

        }

        binding.challengeBtnAdd.setOnClickListener {
            val intent = Intent(this, ChallengeAddActivity::class.java)
            startActivity(intent)
        }

        binding.challengeBtnDelete.setOnClickListener {
            val intent = Intent(this, ChallengeDeleteActivity::class.java)
            startActivity(intent)
        }

        binding.challengeBtnInfo.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }

        binding.challengeBtnConfig.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

    // Draw Bitmap
    @RequiresApi(Build.VERSION_CODES.P)
    public fun updateActivity() {
        val bitmap: Bitmap = Bitmap.createBitmap(950, 1000, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // doneDay만큼 json에서 픽셀 읽어서 그리기
        for (day in 0 until mDoneDay) {
            val dayPixels = imagePixelPerDay.getJSONArray((day+1).toString())
            for(j in 0 until dayPixels.length()) {
                val pixelIndexString = dayPixels.getString(j)
                val pixelIndex = pixelIndexString.split(',')
                val pixelRGBA = imageData.getJSONArray(pixelIndexString)

                val drawRect = Rect(pixelIndex[0].toInt() * 50,
                    pixelIndex[1].toInt() * 50,
                    pixelIndex[0].toInt() * 50 + 50,
                    pixelIndex[1].toInt() * 50 + 50)
                val photoFile = File(
                    File("${filesDir}/image").apply{
                        if(!this.exists()){
                            this.mkdirs()
                        }
                    },
                    "${mSelectedChallengeIndex.toString()}_${(day+1).toString()}.jpg"
                )
                val photoUri = FileProvider.getUriForFile(
                    this,
                    "com.example.battory_app.fileprovider",
                    photoFile
                )

                val imageBitmap = photoUri?.let { ImageDecoder.createSource(this.contentResolver, it) }
                val bitmap = imageBitmap?.let { ImageDecoder.decodeBitmap(it) }
                if (bitmap != null) {
                    val bitmapNotNull = bitmap.copy(Bitmap.Config.ARGB_8888,true)
                    val resizedBitmap = Bitmap.createScaledBitmap(bitmapNotNull,50, 50, true)
                    val paint = Paint()
                    val colorFilter: ColorFilter = PorterDuffColorFilter(Color.rgb(
                        pixelRGBA.getString(0).toInt(),
                        pixelRGBA.getString(1).toInt(),
                        pixelRGBA.getString(2).toInt()), PorterDuff.Mode.OVERLAY)
                    paint.colorFilter = colorFilter

                    val src = Rect(0,0, resizedBitmap.width, resizedBitmap.height)
                    canvas.drawBitmap(resizedBitmap, src, drawRect, paint)
                }
            }
        }
        // 오늘 칠할 수 있는 픽셀 강조 시키기
        if(!mIsAdd && mDoneDay <= imageInfo.getString("total_day").toInt()) {
            val dayPixels = imagePixelPerDay.getJSONArray((mDoneDay+1).toString())
            for(day in 0 until dayPixels.length()) {
                val pixelIndexString = dayPixels.getString(day)
                val pixelIndex = pixelIndexString.split(',')
                //val pixelRGBA = imageData.getJSONArray(pixelIndexString)

                val plusRects = arrayOf(
                    Rect(pixelIndex[0].toInt() * 50 + 23,pixelIndex[1].toInt() * 50 + 17,pixelIndex[0].toInt() * 50 + 27,pixelIndex[1].toInt() * 50 +33),
                    Rect(pixelIndex[0].toInt() * 50 + 17,pixelIndex[1].toInt() * 50 + 23,pixelIndex[0].toInt() * 50 + 33,pixelIndex[1].toInt() * 50 +27)
                )
                val borderRect = Rect(pixelIndex[0].toInt() * 50,
                    pixelIndex[1].toInt() * 50,
                    pixelIndex[0].toInt() * 50 + 50,
                    pixelIndex[1].toInt() * 50 + 50
                )
                val paint = Paint()
                paint.color = Color.rgb(128,128,128)
                canvas.drawRect(plusRects[0], paint)
                canvas.drawRect(plusRects[1], paint)

                paint.style = Paint.Style.STROKE
                canvas.drawRect(borderRect, paint)
            }
        }
        binding.challengeZoomImage.setImageBitmap(bitmap)
        binding.challengeDay.text = "완성도 : ( %d / %d )".format(mDoneDay, imageInfo.getString("total_day").toInt())
    }

    // 오늘의 사진 추가 기능
    public fun onClickToday() {
        if (!mIsAdd){
            val intent = Intent(this, TodayPictureActivity::class.java)
            intent.putExtra("today", mDoneDay + 1)
            intent.putExtra("selectedChallengeIndex", mSelectedChallengeIndex)
            startActivity(intent)
        }
    }

    // ZoomableImage에서 픽셀 클릭시, 있는 픽셀인지 확인 후 기능 실행
    public fun onClickPixel(clickedIndex: Point) {
        // 이미 채워진 사진 보기
        for (day in 0 until mDoneDay) {
            val dayPixels = imagePixelPerDay.getJSONArray((day+1).toString())
            val clickedIndexString = clickedIndex.x.toString() + "," + clickedIndex.y.toString()
            Log.d("clickedIndexString", clickedIndexString.toString())
            for(i in 0 until dayPixels.length()) {
                val pixelIndexString = dayPixels.getString(i)
                val pixelRGBA = imageData.getJSONArray(pixelIndexString)

                if (clickedIndexString == pixelIndexString) {
                    val intent = Intent(this, PastPictureActivity::class.java)
                    intent.putExtra("day", day + 1)
                    intent.putExtra("selectedChallengeIndex", mSelectedChallengeIndex)
                    intent.putExtra("red", pixelRGBA.getString(0).toInt())
                    intent.putExtra("green", pixelRGBA.getString(1).toInt())
                    intent.putExtra("blue", pixelRGBA.getString(2).toInt())
                    startActivity(intent)
                    return
                }
            }
        }
        // 오늘 사진 추가하기
        if(mDoneDay <= imageInfo.getString("total_day").toInt()) {
            val dayPixels = imagePixelPerDay.getJSONArray((mDoneDay + 1).toString())
            val clickedIndexString = clickedIndex.x.toString() + "," + clickedIndex.y.toString()
            for(i in 0 until dayPixels.length()) {
                val pixelIndexString = dayPixels.getString(i)
                if (clickedIndexString == pixelIndexString) {
                    // Click!
                    onClickToday()
                    return
                }
            }
        }

        // Wrong Click
        //binding.challengeName.text = "Wrong Click"
    }
}