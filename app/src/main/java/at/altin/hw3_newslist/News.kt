package at.altin.hw3_newslist

import javax.xml.parsers.DocumentBuilderFactory

/**
 * Data class for the news and its underlying data
 * @author Altin
 * @version 1.0
 * @since 2023-03-19
 */
data class NewsItem(
    var id: Int,
    var title: String,
    var description: String,
    var url: String,
    var author: String,
    var publicationDate: String,
    var fullArticleLink: String,
    var keywords: List<String>
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

/**
 * Converts a list of XML news to a string
 */

fun parseXmlNews(result: String): List<NewsItem> {
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val xml = builder.parse(result.byteInputStream())

    val newsItems = mutableListOf<NewsItem>()
    val news = xml.getElementsByTagName("item")
    for (i in 0 until news.length) {
        val newsItem = news.item(i)
        val title = newsItem.childNodes.item(0).textContent
        val description = newsItem.childNodes.item(1).textContent
        val url = newsItem.childNodes.item(2).textContent
        val author = newsItem.childNodes.item(5).textContent
        val publicationDate = newsItem.childNodes.item(9).textContent
        val fullArticleLink = newsItem.childNodes.item(11).textContent
        val keywords = newsItem.childNodes.item(12).textContent.split(",").map { it.trim() }
        newsItems.add(
            NewsItem(i+1, title, description, url, author, publicationDate, fullArticleLink, keywords)
        )
    }
    return sortNewsByPublicationDate(newsItems)
}

/**
 * Takes a mutable list of news items and sorts them by publication date in descending order
 * @param news the list of news items
 * @return the sorted list
 */
fun sortNewsByPublicationDate(news: MutableList<NewsItem>): List<NewsItem> {
    news.sortByDescending { it.publicationDate }
    return news
}