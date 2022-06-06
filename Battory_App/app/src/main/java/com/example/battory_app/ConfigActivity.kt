package com.example.battory_app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.battory_app.databinding.PageChallengeDeleteBinding
import com.example.battory_app.databinding.PageChallengeAddBinding
import com.example.battory_app.databinding.PageConfigBinding
import com.example.battory_app.databinding.PageInformationBinding
import org.json.JSONArray
import java.io.File

class ConfigActivity : AppCompatActivity() {
    val TAG: String = "ConfigActivity"

    private var mChallengeConfig: PageConfigBinding? = null
    private val challengeConfig get() = mChallengeConfig!!
    private var mLoginIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mChallengeConfig = PageConfigBinding.inflate(layoutInflater)
        setContentView(challengeConfig.root)

        challengeConfig.btnConfigCancel.setOnClickListener{
            WithdrawalLoginDataJson()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        challengeConfig.btnConfigDelete.setOnClickListener{
            LogOutLoginDataJson()
            Toast.makeText(this, "로그아웃 성공 ! \n 로그인 창으로 이동합니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

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
    public fun LogOutLoginDataJson() {
        var loginData = ""

        val jsonPath = "${filesDir}/jsons/LoginData.json"
        if (File(jsonPath).exists()) {
            loginData = File(jsonPath).reader().readText()
        } else {
            loginData = assets.open("LoginData.json").reader().readText()
        }

        val LoginDataJsonArray = JSONArray(loginData)
        val LoginDataJsonObject = LoginDataJsonArray.getJSONObject(mLoginIndex)

        LoginDataJsonArray.getJSONObject(mLoginIndex).put("Success", false)

        val LoginDataJsonPath = "${filesDir}/jsons"
        File(
            File(LoginDataJsonPath).apply{
                if(!this.exists()){
                    this.mkdirs()
                }
            },
            "LoginData.json"
        ).printWriter().use { out -> out.println(LoginDataJsonArray.toString()) }
    }

    public fun WithdrawalLoginDataJson() {
        var loginData = ""

        val jsonPath = "${filesDir}/jsons/LoginData.json"
        if (File(jsonPath).exists()) {
            loginData = File(jsonPath).reader().readText()
        } else {
            loginData = assets.open("LoginData.json").reader().readText()
        }
        val LoginDataJsonArray = JSONArray(loginData)
        val LoginDataJsonObject = LoginDataJsonArray.getJSONObject(mLoginIndex)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Toast.makeText(this, "회원 탈퇴 실패 ! 탈퇴하지 마세요 ! 메인 창으로 이동합니다.", Toast.LENGTH_SHORT).show()
//            LoginDataJsonArray.remove(0)
        }

        val LoginDataJsonPath = "${filesDir}/jsons"
        File(
            File(LoginDataJsonPath).apply {
                if (!this.exists()) {
                    this.mkdirs()
                }
            },
            "LoginData.json"
        ).printWriter().use { out -> out.println(LoginDataJsonArray.toString()) }
    }
}