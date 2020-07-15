package ru.madbrains.githubtask

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    private val url = "https://api.github.com/repositories"

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val queue = Volley.newRequestQueue(this)

        setContentView(R.layout.activity_main)
        getReposFromQueue(queue, 0)
        handler = Handler()

        swipeRefreshLayout.setOnRefreshListener {
            runnable = Runnable {
                getReposFromQueue(queue, 1)
                swipeRefreshLayout.isRefreshing = false
            }
            handler.postDelayed(
                runnable, 3000.toLong()
            )
        }

        swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright
        )
    }

    private fun getReposFromQueue(queue: RequestQueue, i: Int) {
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener { response ->
                val reposList = parseResponse(response, i)
                setList(reposList)
            },
            Response.ErrorListener {
                Toast.makeText(this,"Ошибка запроса", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(stringRequest)
    }

    private fun parseResponse(responseText: String, i: Int): MutableList<Repo> {
        var reposList: MutableList<Repo> = mutableListOf()
        val jsonArray = JSONArray(responseText)
        var length = 3
        if (i == 1) length = jsonArray.length()
        for (index in 0 until length) {
                val jsonObject = jsonArray.getJSONObject(index)
                val name = jsonObject.getString("name")
                val owner = jsonObject.getJSONObject("owner")
                val imageUrl = owner.getString("avatar_url")
                val description = jsonObject.getString("description")
                val login = owner.getString("login")
                val fullName = jsonObject.getString("full_name")
                val repo = Repo(name, imageUrl, description, login, fullName)
                reposList.add(repo)
            }

        return reposList
    }

    private fun setList(repos: List<Repo>) {
        val adapter = ReposAdapter(repos)
        recyclerViewId.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        recyclerViewId.layoutManager = layoutManager
    }
}
