package com.example.battory_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.battory_app.databinding.PageChallengeDeleteBinding
import com.example.battory_app.databinding.PageChallengeAddBinding
import com.example.battory_app.databinding.PageConfigBinding
import com.example.battory_app.databinding.PageInformationBinding
import org.json.JSONObject
import org.json.JSONArray
import java.util.*

class ChallengeAddActivity : AppCompatActivity() {
    val TAG: String = "ChallengeAddActivity"

    private var mChallengeAdd: PageChallengeAddBinding? = null
    private val challengeAdd get() = mChallengeAdd!!

    var challengeDay = 0;
    var challengeName = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mChallengeAdd = PageChallengeAddBinding.inflate(layoutInflater)

        val jsonString = assets.open("ChallengeList.json").reader().readText()
        val jsonArray = JSONArray(jsonString)

        setContentView(challengeAdd.root)

        challengeAdd.btnAddAdd.setOnClickListener(){
            challengeName = challengeAdd.inputAddChallengeName.text.toString()

            var duplicateCheck:Boolean = true
            var index:Int = 0
            var jsonId:String = ""
            while (jsonArray.length() != index) {
                val jsonObject = jsonArray.getJSONObject(index)
                jsonId = jsonObject.getString("ID")
                var jsonTitle = jsonObject.getString("Title")

                if(jsonTitle == challengeName)
                {
                    Toast.makeText(this, "이미 같은 이름의 챌린지가 존재합니다.", Toast.LENGTH_SHORT).show()
                    duplicateCheck = false
                    break
                }
                index++;
            }
            if(duplicateCheck)
            {
                var jObject:JSONObject = JSONObject()
                jObject.put("ID", jsonId.toInt() + 1)
                Toast.makeText(this, jsonId, Toast.LENGTH_SHORT).show()
                jObject.put("Title", challengeName)
                jObject.put("Day", challengeDay)
                jsonArray.put(jObject)
                Toast.makeText(this, "챌린지 생성 완료!", Toast.LENGTH_SHORT).show()
            }
        }

        challengeAdd.btnChallengeDate30.setOnClickListener({
            challengeDay = 30;
        })

        challengeAdd.btnChallengeDate365.setOnClickListener({
            challengeDay = 365;
        })

        challengeAdd.challengeAddBtnHome.setOnClickListener {
            val intent = Intent(this, ChallengeActivity::class.java)
            startActivity(intent)
        }

        challengeAdd.challengeAddBtnAdd.setOnClickListener {
        }

        challengeAdd.challengeAddBtnDelete.setOnClickListener {
            val intent = Intent(this, ChallengeDeleteActivity::class.java)
            startActivity(intent)
        }

        challengeAdd.challengeAddBtnInfo.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }

        challengeAdd.challengeAddBtnConfig.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
        }
    }
}