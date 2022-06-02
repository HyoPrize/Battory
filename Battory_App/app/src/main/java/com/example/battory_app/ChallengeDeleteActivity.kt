package com.example.battory_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.battory_app.databinding.PageChallengeDeleteBinding
import com.example.battory_app.databinding.PageChallengeAddBinding
import com.example.battory_app.databinding.PageConfigBinding
import com.example.battory_app.databinding.PageInformationBinding

class ChallengeDeleteActivity : AppCompatActivity() {
    val TAG: String = "ChallengeDeleteActivity"

    private var mChallengeDelete: PageChallengeDeleteBinding? = null
    private val challengeDelete get() = mChallengeDelete!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mChallengeDelete = PageChallengeDeleteBinding.inflate(layoutInflater)

        setContentView(challengeDelete.root)

        challengeDelete.challengeDeleteBtnHome.setOnClickListener {
            val intent = Intent(this, ChallengeActivity::class.java)
            startActivity(intent)
        }

        challengeDelete.challengeDeleteBtnAdd.setOnClickListener {
            val intent = Intent(this, ChallengeAddActivity::class.java)
            startActivity(intent)
        }

        challengeDelete.challengeDeleteBtnDelete.setOnClickListener {
        }

        challengeDelete.challengeDeleteBtnInfo.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }

        challengeDelete.challengeDeleteBtnConfig.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
        }
    }
}