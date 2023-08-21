package com.manish.newsapp.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.manish.newsapp.data.model.Article
import com.manish.newsapp.data.util.Constants
import com.manish.newsapp.databinding.NewsListItemBinding
import com.squareup.picasso.Picasso

class NewsListAdapter(private val onItemClicked: (Article) -> Unit) :
    ListAdapter<Article, NewsListAdapter.ArticleViewHolder>(CALLBACK_IF_DIFF) {

    inner class ArticleViewHolder(private val binding: NewsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.apply {
                newsTitle.text = article.title
                newsText.text = article.content
                if (article.source?.name?.length!! >10){
                    newsSource.text = "From-".plus(article.source?.name?.subSequence(0,10)?.trim())
                }
                newsSource.text = "From-".plus(article.source?.name)
                newsDate.text = Constants.getDate(article.publishedAt?.trim())
                Picasso.get().load(article.urlToImage).into(binding.newsImage)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ArticleViewHolder {
        return ArticleViewHolder(
            NewsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
        holder.itemView.setOnClickListener {
            onItemClicked(article)
        }
    }

    val differ = AsyncListDiffer(this, CALLBACK_IF_DIFF)

    override fun getItemCount() = differ.currentList.size

    companion object {
        private val CALLBACK_IF_DIFF = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }
}