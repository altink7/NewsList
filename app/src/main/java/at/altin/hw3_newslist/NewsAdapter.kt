package at.altin.hw3_newslist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for the news list
 *  @param news the list of news items
 *  @param context the context of the activity
 *  @author Altin
 *  @version 1.0
 *  @since 2023-03-19
 */
class NewsAdapter(val news: List<NewsItem>, val context: Context) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    var onItemClickListener: ((NewsItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(news[position])
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val id: TextView = itemView.findViewById(R.id.news_nr_id)
        private val title: TextView = itemView.findViewById(R.id.news_title)

        fun bind(newsItem: NewsItem) {
            id.text = buildString { append(newsItem.id); append(": ") }
            title.text = newsItem.title
            onItemClickListener?.invoke(newsItem)

            itemView.setOnClickListener {
                val startNewsDetailActivity = Intent(context, NewsDetailActivity::class.java)
                startNewsDetailActivity.putExtra("id", newsItem.id)
                startNewsDetailActivity.putExtra("title", newsItem.title)
                startNewsDetailActivity.putExtra("description", newsItem.description)
                startNewsDetailActivity.putExtra("url", newsItem.url)
                startNewsDetailActivity.putExtra("author", newsItem.author)
                startNewsDetailActivity.putExtra("publicationDate", newsItem.publicationDate)
                startNewsDetailActivity.putExtra("fullArticleLink", newsItem.fullArticleLink)
                startNewsDetailActivity.putExtra("keywords", newsItem.keywords.toString())
                context.startActivity(startNewsDetailActivity)
            }
        }
    }
}
