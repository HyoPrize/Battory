package com.example.battory_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.battory_app.databinding.PageChallengeDeleteBinding
import com.example.battory_app.databinding.PageChallengeAddBinding
import com.example.battory_app.databinding.PageConfigBinding
import com.example.battory_app.databinding.PageInformationBinding

class InfoActivity : AppCompatActivity() {
    val TAG: String = "InfoActivity"

    private var mChallengeInfo: PageInformationBinding? = null
    private val challengeInfo get() = mChallengeInfo!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mChallengeInfo = PageInformationBinding.inflate(layoutInflater)
        setContentView(challengeInfo.root)

        challengeInfo.infoBtnHome.setOnClickListener {
            val intent = Intent(this, ChallengeActivity::class.java)
            startActivity(intent)
        }

        challengeInfo.infoBtnAdd.setOnClickListener {
            val intent = Intent(this, ChallengeAddActivity::class.java)
            startActivity(intent)
        }

        challengeInfo.infoBtnDelete.setOnClickListener {
            val intent = Intent(this, ChallengeDeleteActivity::class.java)
            startActivity(intent)
        }

        challengeInfo.infoBtnInfo.setOnClickListener {
        }

        challengeInfo.infoBtnConfig.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
        }
    }
}