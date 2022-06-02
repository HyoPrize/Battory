package com.example.battory_app

import android.content.Context
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.battory_app.databinding.PageTodayPictureBinding

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

    private var mToday: Int = 0
    private var mIsAdd = false

    // 사진이 들어간 상태로 들어오는 경우는 없음
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = PageTodayPictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mToday = intent.getIntExtra("today", 0)
        binding.todayPictureName.text = "오늘의 사진 (%d 일차)".format(mToday)

        onAddPicture()

        // 사진 찍기 클릭
        binding.todayPictureCapture.setOnClickListener {
            mIsAdd = true
            onAddPicture()
        }

        // 다시 찍기 클릭
        binding.todayPictureReCapture.setOnClickListener {

        }

        // 사진 저장 클릭
        binding.todayPictureSave.setOnClickListener {

        }

        // 올리기 클릭
        binding.todayPictureUpload.setOnClickListener {

        }

        // 취소 클릭
        binding.todayPictureCancel.setOnClickListener {

        }
    }

    fun onAddPicture() {
        if(mIsAdd) {
            binding.todayPictureBefore.visibility = View.GONE
            binding.todayPictureAfter.visibility = View.VISIBLE
        } else {
            binding.todayPictureBefore.visibility = View.VISIBLE
            binding.todayPictureAfter.visibility = View.GONE
        }
    }
}