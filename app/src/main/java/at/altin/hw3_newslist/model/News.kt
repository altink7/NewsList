package at.altin.hw3_newslist.model

/**
 * Data class for the news and its underlying data
 * @author Altin
 * @version 1.0
 * @since 2023-03-19
 */
data class NewsItem(
    var id: String,
    var title: String,
    var description: String,
    var url: String,
    var author: String,
    var publicationDate: String,
    var fullArticleLink: String,
    var keywords: List<Any>
)

fun newsItemToString(newsItem: NewsItem): String {
    return  "Id: ${newsItem.id} \n" +
            "Title: ${newsItem.title} \n " +
            "Description: ${newsItem.description} \n" +
            "Url: ${newsItem.url} \n" +
            "Author: ${newsItem.author} \n" +
            "PublicationDate: ${newsItem.publicationDate} \n" +
            "FullArticleLink: ${newsItem.fullArticleLink} \n" +
            "Keywords: ${newsItem.keywords} \n"
}
