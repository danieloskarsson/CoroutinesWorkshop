package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

// Task: Implement concurrent loading of articles
suspend fun loadArticlesConcurrently(service: BlogService): List<Article> = coroutineScope {
    service.getArticleInfoList().map {
        async {
            Article(it, service.getComments(it))
        }
    }.awaitAll()
}

suspend fun similarSolution(service: BlogService): List<Article> = coroutineScope {
    service.getArticleInfoList().map {
        async {
            Article(it, service.getComments(it))
        }
    }.map {
        it.await()
    }
}

suspend fun firstSolution(service: BlogService): List<Article> = coroutineScope {
    val articleInfoList = service.getArticleInfoList()
    val articles = mutableListOf<Article>()
    articleInfoList.forEach {
        launch {
            val comments = service.getComments(it)
            articles.add(Article(it, comments))
        }
    }
    return@coroutineScope articles
}

