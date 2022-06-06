package com.example.battory_app

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.battory_app.databinding.JoinMembershipBinding
import org.json.JSONArray
import java.io.File

class Register : AppCompatActivity() {
    val TAG: String = "Register"
    var isExistBlank = false
    var isPWSame = false

    private var mJoinMembership: JoinMembershipBinding? = null;
    private val joinMembership get() = mJoinMembership!!
    private var mLoginIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_membership)
        mJoinMembership = JoinMembershipBinding.inflate(layoutInflater)

        setContentView(joinMembership.root)

        joinMembership.btnJoinCancel.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        joinMembership.btnJoin.setOnClickListener {
            val id = joinMembership.joinInputId.text.toString()
            val pw = joinMembership.joinInputPassword.text.toString()
            val pw_re = joinMembership.joinInputPasswordCheck.text.toString()

            // 유저가 항목을 다 채우지 않았을 경우
            if(id.isEmpty() || pw.isEmpty() || pw_re.isEmpty()){
                isExistBlank = true
            }
            else{
                if(pw == pw_re){
                    isPWSame = true
                }
            }

            if(!isExistBlank && isPWSame){
                // 회원가입 성공 토스트 메세지 띄우기
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                updateLoginDataJson(id,pw)

                // 로그인 화면으로 이동
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
            else{
                // Toast.makeText(this, id + pw + pw_re, Toast.LENGTH_SHORT).show()
                if(isExistBlank){   // 작성 안한 항목이 있을 경우
                    dialog("blank")
                }
                else if(!isPWSame){ // 입력한 비밀번호가 다를 경우
                    dialog("not same")
                }
            }

        }
    }

    // 회원가입 실패시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        // 작성 안한 항목이 있을 경우
        if(type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        }
        // 입력한 비밀번호가 다를 경우
        else if(type.equals("not same")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호가 다릅니다")
        }

        val dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }

    public fun updateLoginDataJson(id:String = "", pw:String = "") {
        var loginData = ""

        val jsonPath = "${filesDir}/jsons/LoginData.json"
        if (File(jsonPath).exists()) {
            loginData = File(jsonPath).reader().readText()
        } else {
            loginData = assets.open("LoginData.json").reader().readText()
        }

        val LoginDataJsonArray = JSONArray(loginData)
        val LoginDataJsonObject = LoginDataJsonArray.getJSONObject(mLoginIndex)

        LoginDataJsonArray.getJSONObject(mLoginIndex).put("ID", id)
        LoginDataJsonArray.getJSONObject(mLoginIndex).put("PW", pw)

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
}