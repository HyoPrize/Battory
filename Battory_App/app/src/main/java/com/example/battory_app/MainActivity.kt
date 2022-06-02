package com.example.battory_app

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.battory_app.databinding.LoginBinding
import com.example.battory_app.databinding.PageChallengeAddBinding
import com.example.battory_app.databinding.PageChallengeDeleteBinding
import org.json.JSONObject
import org.json.JSONArray
import java.io.*

class MainActivity : AppCompatActivity() {
    val TAG: String = "MainActivity"

    private var mLogin: LoginBinding? = null
    private val Login get() = mLogin!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLogin = LoginBinding.inflate(layoutInflater)

        val jsonString = assets.open("LoginData.json").reader().readText()
        val jsonArray = JSONArray(jsonString)
        val jsonObject = jsonArray.getJSONObject(0)

        var jsonId = jsonObject.getString("ID")
        var jsonPw = jsonObject.getString("PW")
        var jsonSuccess = jsonObject.getBoolean("Success")
        var jsonChallengeExist = jsonObject.getBoolean("ChallengeExist")

        if (!jsonSuccess){
            setContentView(Login.root)
        }
        else {
            if(jsonChallengeExist)
            {
                val intent = Intent(this, ChallengeActivity::class.java)
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
                jsonObject.put("Success", true)

                if(jsonChallengeExist)
                {
                    val intent = Intent(this, ChallengeActivity::class.java)
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
}