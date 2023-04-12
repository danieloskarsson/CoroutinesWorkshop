package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
import com.kotlinconf.workshop.articles.network.BlogServiceBlocking

// Task: Implement blocking and non-blocking versions for articles loading

// This function is invoked when you select the "BLOCKING" option in the UI.
fun loadArticles(serviceBlocking: BlogServiceBlocking): List<Article> {
    return serviceBlocking.getArticleInfoList().map {
        Article(it, serviceBlocking.getComments(it))
    }
}

// This function is invoked when you select the "SUSPENDING" option in the UI.
suspend fun loadArticles(service: BlogService): List<Article> {
    return service.getArticleInfoList().map {
        Article(it, service.getComments(it))
    }
}
