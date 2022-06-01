package com.example.battory_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.battory_app.databinding.DemoBinding

class DemoActivity : AppCompatActivity() {
    private var mBinding: DemoBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}