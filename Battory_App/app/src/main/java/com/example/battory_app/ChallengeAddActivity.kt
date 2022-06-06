package com.example.battory_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.battory_app.databinding.PageChallengeAddBinding
import org.json.JSONArray
import java.io.File

class ChallengeAddActivity : AppCompatActivity() {
    val TAG: String = "ChallengeAddActivity"

    private var mChallengeAdd: PageChallengeAddBinding? = null
    private val challengeAdd get() = mChallengeAdd!!
    private var mAddChallengeIndex = 0
    var challengeDay = 0;
    var challengeName = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mChallengeAdd = PageChallengeAddBinding.inflate(layoutInflater)

        val jsonString = assets.open("ChallengeList.json").reader().readText()
        val jsonArray = JSONArray(jsonString)

        setContentView(challengeAdd.root)

        challengeAdd.btnAddAdd.setOnClickListener() {
            challengeName = challengeAdd.inputAddChallengeName.text.toString()
            AddChallengeListJson(challengeName, challengeDay)
        }

        challengeAdd.btnChallengeDate30.setOnClickListener({
            challengeDay = 30;
        })

        challengeAdd.btnChallengeDate365.setOnClickListener({
            challengeDay = 365;
        })

        challengeAdd.challengeAddBtnHome.setOnClickListener {
            finish()
        }

        challengeAdd.challengeAddBtnAdd.setOnClickListener {
        }

        challengeAdd.challengeAddBtnDelete.setOnClickListener {
            val intent = Intent(this, ChallengeDeleteActivity::class.java)
            startActivity(intent)
            finish()
        }

        challengeAdd.challengeAddBtnInfo.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
            finish()
        }

        challengeAdd.challengeAddBtnConfig.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    public fun AddChallengeListJson(title:String = "", day:Int = 0) {
        var challengeList = ""
        val jsonPath = "${filesDir}/jsons/ChallengeList.json"
        if (File(jsonPath).exists()) {
            challengeList = File(jsonPath).reader().readText()
        } else {
            challengeList = assets.open("ChallengeList.json").reader().readText()
        }

        val challengeListJsonArray = JSONArray(challengeList)
        val challengeListJsonObject = challengeListJsonArray.getJSONObject(mAddChallengeIndex)
        var jsonId:String = ""
        var index:Int = 0
        var jsonTitle:String = ""
        var duplicateCheck:Boolean = true
        while (challengeListJsonArray.length() != index) {
            val jsonObject = challengeListJsonArray.getJSONObject(index)

            if (jsonObject.has("ID")) {
                jsonId = jsonObject.getString("ID")
            }

            if (jsonObject.has("Title")) {
                jsonTitle = jsonObject.getString("Title")
            }

            if(jsonTitle == title)
            {
                Toast.makeText(this, "이미 같은 이름의 챌린지가 존재합니다.", Toast.LENGTH_SHORT).show()
                duplicateCheck = false
                break
            }
            index++;
        }

        if(duplicateCheck)
        {
            challengeListJsonArray.getJSONObject(mAddChallengeIndex).put("ID", jsonId.toInt() + 1)
            challengeListJsonArray.getJSONObject(mAddChallengeIndex).put("Title", title)
            challengeListJsonArray.getJSONObject(mAddChallengeIndex).put("Day", day)
            challengeListJsonArray.getJSONObject(mAddChallengeIndex).put("doneDay", 0)
            challengeListJsonArray.getJSONObject(mAddChallengeIndex).put("LastAdd", "")
            challengeListJsonArray.getJSONObject(mAddChallengeIndex).put("Default", false)
            Toast.makeText(this, "챌린지 생성 완료!", Toast.LENGTH_SHORT).show()
        }

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