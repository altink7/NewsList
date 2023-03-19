package at.altin.hw3_newslist

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


/**
 * The ViewModel for the NewsListActivity
 * It loads the news from the web and parses it into a list of News objects
 * It also handles the error state
 */
class NewsViewModel : ViewModel() {
    val logTag = "NewsViewModel"

    val news : LiveData<List<NewsItem>>
        get() = _news
    private val _news = MutableLiveData<List<NewsItem>>()

    val hasError : LiveData<Boolean>
        get() = _hasError
    private val _hasError = MutableLiveData(false)

    fun loadNews() {
        _hasError.postValue(false)
        viewModelScope.launch(Dispatchers.Default) {
            val res = async(Dispatchers.IO) {
                getContentFromWeb();
            }
            when (val result = res.await()) {
                is Success -> {
                    _news.postValue(parseXmlNews(result.result))
                }
                is Failed -> {
                    Log.e(logTag, result.text, result.throwable)
                    _hasError.postValue(true)
                }
            }
        }
    }

    private fun getContentFromWeb(): FetchNewsResult {
        return try {
            val url = URL("https://www.engadget.com/rss.xml")
            (url.openConnection() as HttpURLConnection).run {
                requestMethod = "GET"
                connectTimeout = 5000
                readTimeout = 5000
                String(inputStream.readBytes())
            }.let { Success(it) }
        } catch (ioException: IOException) {
            Failed("Error while fetching news", ioException)
        }
    }
}

sealed class FetchNewsResult
class Success(val result: String) : FetchNewsResult()
class Failed(val text: String, val throwable: Throwable) : FetchNewsResult()