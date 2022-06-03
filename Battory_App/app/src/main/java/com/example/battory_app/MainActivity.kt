package com.example.battory_app

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.battory_app.databinding.LoginBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: MainActivity
        fun ApplicationContext() : Context {
            return instance.applicationContext
        }
    }

    val TAG: String = "MainActivity"

    private var mLogin: LoginBinding? = null
    private val Login get() = mLogin!!

    private var loginData = ""
    private var loginDataJsonArray = JSONArray()
    private var loginDataJsonObject = JSONObject()

    private var jsonId = ""
    private var jsonPw = ""
    private var jsonSuccess = false
    private var jsonChallengeExist = false

    private var challengeList = ""
    private var challengeListJsonArray = JSONArray()
    private var challengeListJsonObject = JSONObject()

    private var mLastAdd = ""
    private var mDoneDay = -1
    private var mSelectedChallengeIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLogin = LoginBinding.inflate(layoutInflater)

        val jsonPath = "${filesDir}/jsons/ChallengeList.json"
        if (File(jsonPath).exists()) {
            Log.d("exist", "exist")
            challengeList = File(jsonPath).reader().readText()
        } else {
            // 파일이 없음
            Log.d("no Exist", "no Exist")
            challengeList = assets.open("ChallengeList.json").reader().readText()
            updateChallengeListJson(-1, "")
        }

        loginData = assets.open("LoginData.json").reader().readText()
        loginDataJsonArray = JSONArray(loginData)
        loginDataJsonObject = loginDataJsonArray.getJSONObject(0)

        jsonId = loginDataJsonObject.getString("ID")
        jsonPw = loginDataJsonObject.getString("PW")
        jsonSuccess = loginDataJsonObject.getBoolean("Success")
        jsonChallengeExist = loginDataJsonObject.getBoolean("ChallengeExist")

        //challengeList = assets.open("ChallengeList.json").reader().readText()
        challengeListJsonArray = JSONArray(challengeList)
        challengeListJsonObject = challengeListJsonArray.getJSONObject(0)

        mLastAdd = challengeListJsonObject.getString("LastAdd")
        mDoneDay = challengeListJsonObject.getInt("doneDay")

        if (!jsonSuccess){
            setContentView(Login.root)
        }
        else {
            if(jsonChallengeExist)
            {
                val now = System.currentTimeMillis()
                val date = Date(now)
                val sdf = SimpleDateFormat("yyyy.MM.dd")
                val today: String = sdf.format(date)

                val intent = Intent(this, ChallengeActivity::class.java)
                intent.putExtra("isAdd", today == mLastAdd)
                intent.putExtra("selectedChallengeIndex", 0)
                intent.putExtra("doneDay", mDoneDay)
                startActivity(intent)
            }
            else
            {
                val intent = Intent(this, ChallengeAddActivity::class.java)
                startActivity(intent)
            }
        }

        Login.btnLogin.setOnClickListener {
            var id: String = Login.inputId.text.toString()
            var pw: String = Login.inputPassword.text.toString()

            if(id == jsonId && pw == jsonPw){
                // 로그인 성공 다이얼로그 보여주기
                dialog("login_success")
                loginDataJsonObject.put("Success", true)

                if(jsonChallengeExist)
                {
                    val now = System.currentTimeMillis()
                    val date = Date(now)
                    val sdf = SimpleDateFormat("yyyy.MM.dd")
                    val today: String = sdf.format(date)

                    val intent = Intent(this, ChallengeActivity::class.java)
                    intent.putExtra("isAdd", today == mLastAdd)
                    intent.putExtra("selectedChallengeIndex", 0)
                    intent.putExtra("doneDay", mDoneDay)
                    startActivity(intent)
                }
                else
                {
                    val intent = Intent(this, ChallengeAddActivity::class.java)
                    startActivity(intent)
                }
            }
            else{
                // 로그인 실패 다이얼로그 보여주기
                dialog("login_fail")
            }
        }

        // 회원가입 버튼
        Login.btnJoinMembership.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    // 로그인 성공/실패 시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)

        if(type.equals("login_success")){
            dialog.setTitle("로그인 성공")
            dialog.setMessage("로그인 성공!")
        }
        else if(type.equals("login_fail")){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }

        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }

    public fun updateChallengeListJson(doneDay:Int = -1, lastAdd:String = "") {
        var challengeList = ""

        val jsonPath = "${filesDir}/jsons/ChallengeList.json"
        if (File(jsonPath).exists()) {
            challengeList = File(jsonPath).reader().readText()
        } else {
            challengeList = assets.open("ChallengeList.json").reader().readText()
        }

        val challengeListJsonArray = JSONArray(challengeList)
        val challengeListJsonObject = challengeListJsonArray.getJSONObject(mSelectedChallengeIndex)

        if(doneDay != -1)
            challengeListJsonArray.getJSONObject(mSelectedChallengeIndex).put("doneDay", doneDay)
        if(lastAdd != "")
            challengeListJsonArray.getJSONObject(mSelectedChallengeIndex).put("LastAdd", lastAdd)

        val challengeListJsonPath = "${filesDir}/jsons"
        File(
            File(challengeListJsonPath).apply{
                if(!this.exists()){
                    this.mkdirs()
                }
            },
            "ChallengeList.json"
        ).printWriter().use { out -> out.println(challengeListJsonArray.toString()) }
    }
}