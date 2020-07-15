package ru.madbrains.githubtask

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.madbrains.githubtask.DetailRepoActivity.Companion.FULL_NAME

class ReposAdapter(private val repos: List<Repo>) : RecyclerView.Adapter<RepoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val rootView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.repo, parent, false)
        return RepoViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(repos[position])
    }
}

class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageUrl: ImageView = itemView.findViewById(R.id.imageViewAvatar)
    private val name: TextView = itemView.findViewById(R.id.textViewRepoName)
    private val description: TextView = itemView.findViewById(R.id.textViewRepoDescription)
    private val login: TextView = itemView.findViewById(R.id.textViewAuthor)

    fun bind(repo: Repo) {
        name.text = repo.name
        description.text = repo.description
        login.text = repo.login

        Glide.with(itemView).load(repo.imageUrl).into(imageUrl)

        itemView.setOnClickListener {
            openActivity(itemView.context, repo)
        }
    }

    private fun openActivity (context: Context, repo: Repo) {
        val intent = Intent(context, DetailRepoActivity::class.java)
        intent.putExtra(FULL_NAME, repo.fullName)
        context.startActivity(intent)
    }
}