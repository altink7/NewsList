package at.altin.hw3_newslist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

class NewsAdapter(val news: List<NewsItem>, val context: Context) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    var onItemClickListener: ((NewsItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
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
        private val description: TextView = itemView.findViewById(R.id.news_description)
        private val url: TextView = itemView.findViewById(R.id.news_url)
        private val author: TextView = itemView.findViewById(R.id.news_author)
        private val publicationDate: TextView = itemView.findViewById(R.id.news_publication_date)
        private val fullArticleLink: TextView = itemView.findViewById(R.id.news_full_article_link)
        private val keywords: TextView = itemView.findViewById(R.id.news_keywords)

        fun bind(newsItem: NewsItem) {
            id.text = buildString { append(newsItem.id); append(": ") }
            title.text = newsItem.title
//            description.text = newsItem.description
//            url.text = newsItem.url
//            author.text = newsItem.author
//            publicationDate.text = newsItem.publicationDate
//            fullArticleLink.text = newsItem.fullArticleLink
//            keywords.text = newsItem.keywords.toString()
            onItemClickListener?.invoke(newsItem)

            itemView.setOnClickListener {
                onItemClickListener?.invoke(newsItem)
            }
        }
    }
}
