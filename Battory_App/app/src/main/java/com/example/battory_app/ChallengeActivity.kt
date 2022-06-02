package com.example.battory_app

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.battory_app.databinding.ChallengeBinding
import org.json.JSONArray
import org.json.JSONObject
import java.util.logging.Filter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.challengeZoomImage.setImageBitmap(bitmap)

        // 챌린지 관련 세팅
        binding.challengeName.text = "오늘까지 개발 끝내기"
        binding.challengeCompleteRate.text = "완성도 : " + doneDay.toString() + "/"+ imageInfo.getString("total_day")

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

    // ZoomableImage에서 픽셀 클릭시, 있는 픽셀인지 확인 후 기능 실행
    public fun onClickPixel(clickedIndex: Point) {
        for (day in 0 until doneDay) {
            val dayPixels = imagePixelPerDay.getJSONArray((day+1).toString())
            val clickedIndexString = clickedIndex.x.toString() + "," + clickedIndex.y.toString()
            Log.d("clickedIndexString", clickedIndexString.toString())
            for(j in 0 until dayPixels.length()) {
                val pixelIndexString = dayPixels.getString(j)
                if (clickedIndexString == pixelIndexString) {
                    // Click!
                    binding.challengeName.text = clickedIndex.toString() + " / " + (day+1).toString()+"일 기록"
                    return
                }
            }
        }
        // Wrong Click
        binding.challengeName.text = "Wrong Click"
    }
}