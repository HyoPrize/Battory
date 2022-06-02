package com.example.battory_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.battory_app.databinding.PageChallengeDeleteBinding
import com.example.battory_app.databinding.PageChallengeAddBinding
import com.example.battory_app.databinding.PageConfigBinding
import com.example.battory_app.databinding.PageInformationBinding

class ConfigActivity : AppCompatActivity() {
    val TAG: String = "ConfigActivity"

    private var mChallengeConfig: PageConfigBinding? = null
    private val challengeConfig get() = mChallengeConfig!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mChallengeConfig = PageConfigBinding.inflate(layoutInflater)
        setContentView(challengeConfig.root)

        challengeConfig.configBtnHome.setOnClickListener {
            finish()
        }

        challengeConfig.configBtnAdd.setOnClickListener {
            val intent = Intent(this, ChallengeAddActivity::class.java)
            startActivity(intent)
            finish()
        }

        challengeConfig.configBtnDelete.setOnClickListener {
            val intent = Intent(this, ChallengeDeleteActivity::class.java)
            startActivity(intent)
            finish()
        }

        challengeConfig.configBtnInfo.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
            finish()
        }

        challengeConfig.configBtnConfig.setOnClickListener {
        }
    }
}