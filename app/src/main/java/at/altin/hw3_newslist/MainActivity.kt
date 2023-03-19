package at.altin.hw3_newslist

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import at.altin.hw3_newslist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainActivityBinding: ActivityMainBinding

    private val logTag = "MainActivity"

    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        mainActivityBinding.lifecycleOwner = this
        mainActivityBinding.newsViewModel = newsViewModel

        newsViewModel.news.observe(this) { it ->
            val newsAdapter = NewsAdapter(it, this)
            newsAdapter.onItemClickListener = {
                Log.i(logTag, "News clicked: $it")
            }
            mainActivityBinding.newsRecyclerView.adapter = newsAdapter
        }

        newsViewModel.hasError.observe(this) {
            if (it) {
                Toast.makeText(this@MainActivity, getString(R.string.loading_error_text), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
