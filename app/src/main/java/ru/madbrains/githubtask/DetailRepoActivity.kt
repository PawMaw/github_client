package ru.madbrains.githubtask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_detail_repo.*
import org.json.JSONObject

class DetailRepoActivity : AppCompatActivity() {
    companion object {
        const val FULL_NAME = "Repository details"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_repo)
        val queue = Volley.newRequestQueue(this)
        getRequestFromQueue(queue)
        setupActionBar()
    }

    private fun setupActionBar () {
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = FULL_NAME
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun getRequestFromQueue(queue: RequestQueue) {
        val repoDirectory = intent?.extras?.getString(FULL_NAME)
        val stringRequest = StringRequest(
            Request.Method.GET,
            "https://api.github.com/repos/$repoDirectory",
            Response.Listener { response ->
                val detailsArrayListOfString = createRequest(response)
                setText(detailsArrayListOfString)
            },
            Response.ErrorListener {
                Toast.makeText(this,"Ошибка запроса", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(stringRequest)
    }

    private fun createRequest(rawResult: String): ArrayList<String> {
        var jsonArray = JSONObject(rawResult)
            val stars = jsonArray.getString("stargazers_count")
            val forksCount = jsonArray.getString("forks_count")
            val language = jsonArray.getString("language")
            val description = jsonArray.getString("description")

        return arrayListOf(stars, forksCount, language, description)
    }

    private fun setText(detailsArrayListOfString: ArrayList<String>) {
        textViewDetailsStars.text = detailsArrayListOfString.component1()
        textViewDetailsForks.text = detailsArrayListOfString.component2()
        textViewDetailsLanguage.text = detailsArrayListOfString.component3()
        textViewDetailsDescription.text = detailsArrayListOfString.component4()
    }
}