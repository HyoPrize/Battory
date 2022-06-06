package com.example.battory_app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.battory_app.databinding.PageChallengeDeleteBinding
import org.json.JSONArray
import java.io.File

class ChallengeDeleteActivity : AppCompatActivity() {
    val TAG: String = "ChallengeDeleteActivity"

    private var mChallengeDelete: PageChallengeDeleteBinding? = null
    private val challengeDelete get() = mChallengeDelete!!
    private var mShowChallengeIndex:Int = 0;
    var btnDel:Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mChallengeDelete = PageChallengeDeleteBinding.inflate(layoutInflater)

        var btnList = listOf(challengeDelete.btnDelChallenge1,
            challengeDelete.btnDelChallenge2, challengeDelete.btnDelChallenge3, challengeDelete.btnDelChallenge4)

        for(data in btnList)
            data.visibility = View.GONE

        var challengeList = ""
        val jsonPath = "${filesDir}/jsons/ChallengeList.json"
        if (File(jsonPath).exists()) {
            challengeList = File(jsonPath).reader().readText()
        } else {
            challengeList = assets.open("ChallengeList.json").reader().readText()
        }
        ShowChallengeListJson(challengeList, btnList)

        setContentView(challengeDelete.root)

        challengeDelete.btnDelChallenge1.setOnClickListener{
            btnDel = challengeDelete.btnDelChallenge1
        }

        challengeDelete.btnDelChallenge2.setOnClickListener{
            btnDel = challengeDelete.btnDelChallenge2
        }

        challengeDelete.btnDeleteDelete.setOnClickListener{
            DelChallengeListJson(challengeList , btnDel?.text.toString())
            btnDel?.visibility = View.GONE
        }

        challengeDelete.challengeDeleteBtnHome.setOnClickListener {
            finish()
        }

        challengeDelete.challengeDeleteBtnAdd.setOnClickListener {
            val intent = Intent(this, ChallengeAddActivity::class.java)
            startActivity(intent)
            finish()
        }

        challengeDelete.challengeDeleteBtnDelete.setOnClickListener {
        }

        challengeDelete.challengeDeleteBtnInfo.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
            finish()
        }

        challengeDelete.challengeDeleteBtnConfig.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    public fun DelChallengeListJson(challengeList:String = "", btnName:String = "") {
        val challengeListJsonArray = JSONArray(challengeList)
        val challengeListJsonObject = challengeListJsonArray.getJSONObject(mShowChallengeIndex)
        var index:Int = 0
        while (2 != index) {
            val jsonObject = challengeListJsonArray.getJSONObject(index)
            var jsonTitle = jsonObject.getString("Title")
            if(btnName.contains(jsonTitle))
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Toast.makeText(this, "챌린지 삭제 완료!", Toast.LENGTH_SHORT).show()
                    challengeListJsonArray.remove(index)
                }
            }
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

        while(challengeListJsonArray.length() != index) {
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
        for(title in titleList)
        {
            btnList[index].text = title
            btnList[index].visibility = View.VISIBLE
            index++
        }
    }
}