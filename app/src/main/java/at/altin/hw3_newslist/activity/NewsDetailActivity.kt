package at.altin.hw3_newslist.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import at.altin.hw3_newslist.recyclerView.NewsDetailAdapter
import at.altin.hw3_newslist.NewsViewModel
import at.altin.hw3_newslist.R
import at.altin.hw3_newslist.databinding.ActivityNewsDetailBinding
import at.altin.hw3_newslist.model.NewsItem

/**
 * NewsDetailActivity
 * @author altin
 * @version 1.0
 * @since 2023-03-19
 */
class NewsDetailActivity : AppCompatActivity() {
    private lateinit var newsDetailActivityBinding: ActivityNewsDetailBinding

    private val logTag = "NewsDetailActivity"

    private val newsViewModel: NewsViewModel by viewModels()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsDetailActivityBinding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(newsDetailActivityBinding.root)

        newsDetailActivityBinding.lifecycleOwner = this
        newsDetailActivityBinding.newsViewModel = newsViewModel

        val id = intent.getIntExtra("id",0)
        val title = intent.getStringExtra("title")?: ""
        val description = intent.getStringExtra("description")?: ""
        val url = intent.getStringExtra("image")?: ""
        val author = intent.getStringExtra("author")?: ""
        val date = intent.getStringExtra("date")?: ""
        val fullArticleLink = intent.getStringExtra("fullArticleLink")?: ""

            val newsDetailAdapter = NewsDetailAdapter(NewsItem(id,title, description,url,author,date,fullArticleLink, emptyList()), this)
            newsDetailAdapter.onItemClickListener = {
                Log.i(logTag, "News clicked: $it")
            }
            newsDetailActivityBinding.newsRecyclerView.adapter = newsDetailAdapter


        newsViewModel.hasError.observe(this) {
            if (it) {
                Toast.makeText(this@NewsDetailActivity, getString(R.string.loading_error_text), Toast.LENGTH_LONG)
                    .show()
            }
        }

        val button = findViewById<Button>(R.id.navigateBack)
        button.setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.fullStory)?.setOnClickListener {
            val descriptionParam = findViewById<TextView>(R.id.news_description)
            val layoutParam = descriptionParam.layoutParams as ConstraintLayout.LayoutParams
            layoutParam.height = 1400
            descriptionParam.layoutParams = layoutParam
            findViewById<Button>(R.id.fullStory).isEnabled = false
        }
    }
}

