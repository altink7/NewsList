package at.altin.hw3_newslist.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.allViews
import androidx.core.view.get
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import at.altin.hw3_newslist.NewsViewModel
import at.altin.hw3_newslist.R
import at.altin.hw3_newslist.databinding.ActivityMainBinding
import at.altin.hw3_newslist.model.NewsItem
import at.altin.hw3_newslist.recyclerView.NewsAdapter
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var mainActivityBinding: ActivityMainBinding
    private val logTag = "MainActivity"
    val showImages = booleanPreferencesKey("displayImages")

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

        mainActivityBinding.openSettings.setOnClickListener {
            Intent(applicationContext, SettingsActivity::class.java).also {
                startActivity(it)
            }
        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.toString()
        val signature = prefs.getString("sinature", "")
        if(signature != null) {
            Log.i(logTag, "Signature: $signature")
        }

        prefs.registerOnSharedPreferenceChangeListener(this)

        lifecycleScope.launch {
            applicationContext.dataStore.edit {
                it[showImages] = true
                val currentValue = it[showImages]
                }
            }
        lifecycleScope.launch {
            val dataStorePrefs = applicationContext
                .dataStore.data.first()

            val currentValue = dataStorePrefs[showImages]
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "displayImages"){
            val displayImages = sharedPreferences?.getBoolean(key, true)
            Log.i(logTag, "Display images: $displayImages")

            if(displayImages == true) {
                mainActivityBinding.newsRecyclerView.allViews.forEach {
                    if(it is ImageView) {
                        it.visibility = View.VISIBLE
                    }
                }
            } else {
                mainActivityBinding.newsRecyclerView.allViews.forEach {
                    if(it is ImageView) {
                        it.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuitem_settings) {
            Intent(applicationContext, SettingsActivity::class.java)
                .also { startActivity(it) }
            return true
        }
        if(item.itemId == R.id.button_load_data) {
            newsViewModel.loadNews()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
