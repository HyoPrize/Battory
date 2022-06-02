package com.example.battory_app

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.battory_app.databinding.ChallengeBinding
import org.json.JSONObject

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
    private var doneDay = 0
    private var jsonString:String = ""
    private var jsonArray = JSONObject()
    private var imageInfo = JSONObject()
    private var imageData = JSONObject()
    private var imagePixelPerDay = JSONObject()

    private var mIsAdd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mIsAdd = intent.getBooleanExtra("isAdd", false)

        // Save json 추가시 doneDay 날짜 선택 필요
        doneDay = 1
        jsonString = assets.open("pikachu_info.json").reader().readText()
        jsonArray = JSONObject(jsonString)
        imageInfo = jsonArray.getJSONObject("info")
        imageData = jsonArray.getJSONObject("data")
        imagePixelPerDay = jsonArray.getJSONObject("pixel_per_day")

        // bitmap & canvas 생성(픽셀 그리기)
        val bitmap: Bitmap = Bitmap.createBitmap(950, 1000, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // doneDay만큼 json에서 픽셀 읽어서 그리기
        for (i in 0 until doneDay) {
            val dayPixels = imagePixelPerDay.getJSONArray((i+1).toString())
            for(j in 0 until dayPixels.length()) {
                val pixelIndexString = dayPixels.getString(j)
                val pixelIndex = pixelIndexString.split(',')
                val pixelRGBA = imageData.getJSONArray(pixelIndexString)

                val drawRect = Rect(pixelIndex[0].toInt() * 50,
                    pixelIndex[1].toInt() * 50,
                    pixelIndex[0].toInt() * 50 + 50,
                    pixelIndex[1].toInt() * 50 + 50)
                val paint = Paint()
                paint.color = Color.rgb(pixelRGBA.getString(0).toInt(), pixelRGBA.getString(1).toInt(), pixelRGBA.getString(2).toInt())
                canvas.drawRect(drawRect, paint)
            }
        }
        // 오늘 칠할 수 있는 픽셀 강조 시키기
        if(!mIsAdd && doneDay <= imageInfo.getString("total_day").toInt()) {
            val dayPixels = imagePixelPerDay.getJSONArray((doneDay+1).toString())
            for(i in 0 until dayPixels.length()) {
                val pixelIndexString = dayPixels.getString(i)
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

        // 챌린지 관련 세팅
        binding.challengeName.text = "오늘까지 개발 끝내기"
        binding.challengeDay.text = "완성도 : ( %d / %d )".format(doneDay, imageInfo.getString("total_day").toInt())

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

    // 오늘의 사진 추가 기능
    public fun onClickToday() {
        val intent = Intent(this, TodayPictureActivity::class.java)
        intent.putExtra("today", doneDay + 1)
        startActivity(intent)
    }

    // ZoomableImage에서 픽셀 클릭시, 있는 픽셀인지 확인 후 기능 실행
    public fun onClickPixel(clickedIndex: Point) {
        // 이미 채워진 사진 보기
        for (day in 0 until doneDay) {
            val dayPixels = imagePixelPerDay.getJSONArray((day+1).toString())
            val clickedIndexString = clickedIndex.x.toString() + "," + clickedIndex.y.toString()
            Log.d("clickedIndexString", clickedIndexString.toString())
            for(i in 0 until dayPixels.length()) {
                val pixelIndexString = dayPixels.getString(i)
                if (clickedIndexString == pixelIndexString) {
                    // Click!
                    //binding.challengeName.text = clickedIndex.toString() + " / " + (day+1).toString()+"일 기록"
                    return
                }
            }
        }
        // 오늘 사진 추가하기
        if(doneDay <= imageInfo.getString("total_day").toInt()) {
            val dayPixels = imagePixelPerDay.getJSONArray((doneDay + 1).toString())
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