package com.hugomatilla.starwars.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hugomatilla.starwars.R
import com.hugomatilla.starwars.data.cloud.ArticleRepository
import com.hugomatilla.starwars.domain.ArticleDomain
import com.hugomatilla.starwars.domain.GetArticleListUseCase
import com.hugomatilla.starwars.domain.IGetArticleListUseCase
import kotlinx.android.synthetic.main.articles_list_activity.*
import org.jetbrains.anko.toast
import java.util.concurrent.Executors

class ArticlesListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.articles_list_activity)
        listView.layoutManager = LinearLayoutManager(this)
        progressBar.visibility = View.VISIBLE

    }

    override fun onResume() {
        super.onResume()
        val threadPool = Executors.newFixedThreadPool(5)
        val articleRepository = ArticleRepository
        GetArticleListUseCase(articleRepository, threadPool).execute(object : IGetArticleListUseCase.Callback {
            override fun onListLoaded(articles: Collection<ArticleDomain>?) {
                progressBar.visibility = View.GONE
                inflateArticles(articles)
            }

            override fun onError(error: String) {
                progressBar.visibility = View.GONE
                toast("Error")
            }
        })
    }

    private fun inflateArticles(articles: Collection<ArticleDomain>?) {
        if (articles != null)
            listView.adapter = ArticlesListAdapter(articles, { toast(it.title) })
        else
            toast("No articles to show. :)")
    }

}
