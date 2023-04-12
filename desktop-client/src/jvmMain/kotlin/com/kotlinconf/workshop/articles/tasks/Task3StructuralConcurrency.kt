package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

// Run both versions and try to cancel
suspend fun loadArticlesNonCancelable(service: BlogService): List<Article> {
    val articleInfoList = service.getArticleInfoList()
    val deferreds = articleInfoList.map {
        GlobalScope.async {
            Article(it, service.getComments(it))
        }
    }
    return deferreds.awaitAll()
}
