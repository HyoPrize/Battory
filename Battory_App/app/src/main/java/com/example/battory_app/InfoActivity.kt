package com.example.battory_app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.example.battory_app.databinding.PageChallengeDeleteBinding
import com.example.battory_app.databinding.PageChallengeAddBinding
import com.example.battory_app.databinding.PageConfigBinding
import com.example.battory_app.databinding.PageInformationBinding
import org.json.JSONArray
import java.io.File

class InfoActivity : AppCompatActivity() {
    val TAG: String = "InfoActivity"

    private var mChallengeInfo: PageInformationBinding? = null
    private val challengeInfo get() = mChallengeInfo!!
    private var mShowChallengeIndex:Int = 0;
    var btnInfo:Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mChallengeInfo = PageInformationBinding.inflate(layoutInflater)

        var btnList = listOf(challengeInfo.btnInfoChallenge1,
            challengeInfo.btnInfoChallenge2, challengeInfo.btnInfoChallenge3, challengeInfo.btnInfoChallenge4)

        for(btn in btnList)
            btn.visibility = View.GONE

        var challengeList = ""
        val jsonPath = "${filesDir}/jsons/ChallengeList.json"
        if (File(jsonPath).exists()) {
            challengeList = File(jsonPath).reader().readText()
        } else {
            challengeList = assets.open("ChallengeList.json").reader().readText()
        }
        ShowChallengeListJson(challengeList, btnList)

        setContentView(challengeInfo.root)

        challengeInfo.btnInfoChallenge1.setOnClickListener{
            btnInfo = challengeInfo.btnInfoChallenge1
        }

        challengeInfo.btnInfoChallenge2.setOnClickListener{
            btnInfo = challengeInfo.btnInfoChallenge2
        }

        challengeInfo.btnInfoSelect.setOnClickListener{
            SetDefaultChallengeListJson(challengeList,btnInfo?.text.toString())
        }

        challengeInfo.infoBtnHome.setOnClickListener {
            finish()
        }

        challengeInfo.infoBtnAdd.setOnClickListener {
            val intent = Intent(this, ChallengeAddActivity::class.java)
            startActivity(intent)
            finish()
        }

        challengeInfo.infoBtnDelete.setOnClickListener {
            val intent = Intent(this, ChallengeDeleteActivity::class.java)
            startActivity(intent)
            finish()
        }

        challengeInfo.infoBtnInfo.setOnClickListener {
        }

        challengeInfo.infoBtnConfig.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    public fun SetDefaultChallengeListJson(challengeList:String = "", challengeName:String = "") {
        val challengeListJsonArray = JSONArray(challengeList)
        val challengeListJsonObject = challengeListJsonArray.getJSONObject(mShowChallengeIndex)
        var jsonId:String = ""
        var index:Int = 0
        var titleList:ArrayList<String> = ArrayList()

        while (challengeListJsonArray.length() != index) {
            val jsonObject = challengeListJsonArray.getJSONObject(index)
            var jsonTitle = jsonObject.getString("Title")
            if(challengeName.contains(jsonTitle)) {
                jsonObject.put("Default", true)
                Toast.makeText(this, challengeName + " 챌린지를 Default 챌린지로 지정 완료!", Toast.LENGTH_SHORT).show()
            }
            else
                jsonObject.put("Default", false)

            index++;
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

    public fun ShowChallengeListJson(challengeList:String = "", btnList:List<Button>) {
        val challengeListJsonArray = JSONArray(challengeList)
        val challengeListJsonObject = challengeListJsonArray.getJSONObject(mShowChallengeIndex)
        var index:Int = 0
        var titleList:ArrayList<String> = ArrayList()

        while (challengeListJsonArray.length() != index) {
            val jsonObject = challengeListJsonArray.getJSONObject(index)

            var jsonTitle:String = ""
            if (jsonObject.has("Title")) {
                jsonTitle = jsonObject.getString("Title")
            }
            if(jsonTitle != "")
                titleList.add(jsonTitle)

            index++;
        }

        index = 0
        for (title in titleList)
        {
            btnList[index].text = title
            btnList[index].visibility = View.VISIBLE
            index++
        }
    }
}