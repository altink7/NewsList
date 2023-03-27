package at.altin.hw3_newslist.recyclerView


import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import at.altin.hw3_newslist.R
import at.altin.hw3_newslist.model.NewsItem
import com.bumptech.glide.Glide

/**
 * Adapter for the news list
 *  @param news the list of news items
 *  @param context the context of the activity
 *  @author Altin
 *  @version 1.0
 *  @since 2023-03-19
 */
class NewsDetailAdapter(val news: NewsItem, val context: Context) : RecyclerView.Adapter<NewsDetailAdapter.NewsViewHolder>() {

    var onItemClickListener: ((String) -> Unit)? = null
    override fun getItemCount() = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(news)
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.news_title)
        private val author: TextView = itemView.findViewById(R.id.news_author)
        private val date: TextView = itemView.findViewById(R.id.news_date)
        private val image: ImageView = itemView.findViewById(R.id.news_image)
        private val description: TextView = itemView.findViewById(R.id.news_description)

        fun bind(news: NewsItem) {
            this.title.text = news.title
            this.author.text = news.author
            this.date.text = news.publicationDate
            this.description.text = Html.fromHtml(news.description, Html.FROM_HTML_MODE_COMPACT)

            Glide
                .with(context)
                .load(news.url)
                .placeholder(R.drawable.ic_launcher_background)
                .into(image)

            itemView.setOnClickListener {
                onItemClickListener?.invoke(news.toString())
            }
        }
    }
}