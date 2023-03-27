package at.altin.hw3_newslist.recyclerView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import at.altin.hw3_newslist.R
import at.altin.hw3_newslist.activity.NewsDetailActivity
import at.altin.hw3_newslist.model.NewsItem
import at.altin.hw3_newslist.model.newsItemToString
import com.bumptech.glide.Glide

/**
 * Adapter for the news list
 *  @param news the list of news items
 *  @param context the context of the activity
 *  @author Altin
 *  @version 1.0
 *  @since 2023-03-19
 */
class NewsAdapter(news: List<NewsItem>, val context: Context) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(){
    var displayImages: Boolean = true

    var news = news
        set(value) {
        field = value
        notifyDataSetChanged()
        }

    var onItemClickListener: ((NewsItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(news[position],position, displayImages)
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.news_title)
        private val author: TextView = itemView.findViewById(R.id.news_author)
        private val date: TextView = itemView.findViewById(R.id.news_date)
        private val image: ImageView = itemView.findViewById(R.id.news_image)

        fun bind(newsItem: NewsItem, position: Int, displayImages: Boolean) {
            title.text = newsItem.title
            author.text = newsItem.author
            date.text = newsItem.publicationDate

            Glide
                .with(context)
                .load(newsItem.url)
                .placeholder(R.drawable.ic_launcher_background)
                .into(image)

            if (position == 0) {
                    val layoutParams = image.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT
                    layoutParams.height = 500
                    image.layoutParams = layoutParams
                    image.scaleType = ImageView.ScaleType.FIT_XY
                    image.alpha = 0.4f

                fun setLayoutParams(view: View) {
                    val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
                    view.alpha = 1f

                    layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

                    view.bringToFront()
                    view.layoutParams = layoutParams
                }

                setLayoutParams(title)
                setLayoutParams(author)
                setLayoutParams(date)
            }
             if(position!=0){
                val layoutParams = image.layoutParams as ConstraintLayout.LayoutParams
                 layoutParams.height = 150
                 layoutParams.width = 280
                 image.layoutParams = layoutParams
                 image.scaleType = ImageView.ScaleType.FIT_XY
                 image.alpha = 1f
                }

            if (displayImages) {
                image.visibility = View.VISIBLE
            } else {
                image.visibility = View.GONE
            }

            onItemClickListener?.invoke(newsItem)

            itemView.setOnClickListener {
                val startNewsDetailActivity = Intent(context, NewsDetailActivity::class.java).also {
                    it.putExtra("id", newsItem.id)
                    it.putExtra("title", newsItem.title)
                    it.putExtra("author", newsItem.author)
                    it.putExtra("date", newsItem.publicationDate.toString())
                    it.putExtra("image", newsItem.url)
                    it.putExtra("description", newsItem.description)
                    it.putExtra("fullArticleLink", newsItem.fullArticleLink)
                }
                context.startActivity(startNewsDetailActivity)
            }
        }
    }
}