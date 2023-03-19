package at.altin.hw3_newslist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import at.altin.hw3_newslist.databinding.ActivityNewsDetailBinding

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

        val item = intent.getStringExtra("newsItem").toString()

            val newsDetailAdapter = NewsDetailAdapter(item, this)
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
    }
}

