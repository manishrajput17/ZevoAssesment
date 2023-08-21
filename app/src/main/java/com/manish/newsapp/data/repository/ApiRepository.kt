package com.manish.newsapp.data.repository

import com.manish.newsapp.data.local.ArticleDatabase
import com.manish.newsapp.data.model.Article
import com.manish.newsapp.data.network.NewsApi
import com.manish.newsapp.data.util.Constants.Companion.API_KEY
import com.manish.newsapp.data.util.Constants.Companion.DEFAULT_COUNTRY
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val database: ArticleDatabase,
    private val newsApi: NewsApi,
) {

    suspend fun getAllArticles(pageNumber: Int) =
        newsApi.getNews(DEFAULT_COUNTRY, pageNumber, API_KEY)

    fun getFavoriteArticles() = database.articleDao().getArticles()

    suspend fun insert(article: Article) = database.articleDao().insert(article)

    suspend fun deleteArticle(article: Article) = database.articleDao().deleteArticle(article)
}