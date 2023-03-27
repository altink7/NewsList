package at.altin.hw3_newslist

import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.altin.hw3_newslist.model.NewsItem
import at.altin.hw3_newslist.model.RssParser
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
 * @author Altin
 *  @version 1.0
 *  @since 2023-03-19
 */
class NewsViewModel : ViewModel() {
    val logTag = "NewsViewModel"

    val news : LiveData<List<NewsItem>>
        get() = _news
    private val _news = MutableLiveData<List<NewsItem>>()


    val hasError : LiveData<Boolean>
        get() = _hasError
    private val _hasError = MutableLiveData(false)

    fun loadNews(url:String) {
        _hasError.postValue(false)
        viewModelScope.launch(Dispatchers.Default) {
            val res = async(Dispatchers.IO) {
                getContentFromWeb(url);
            }
            when (val result = res.await()) {
                is Success -> {
                    _news.postValue(RssParser().parse(result.result.byteInputStream()))
                }
                is Failed -> {
                    Log.e(logTag, result.text, result.throwable)
                    _hasError.postValue(true)
                }
                else -> {
                    Log.e(logTag, "Unknown result type")
                    _hasError.postValue(true)
                }
            }
        }
    }

    private fun getContentFromWeb(url:String): FetchNewsResult {
        return try {
            val url = URL(url)
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