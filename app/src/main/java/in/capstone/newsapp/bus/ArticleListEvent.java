package in.capstone.newsapp.bus;

import java.util.List;

import in.capstone.newsapp.models.Article;

/**
 * Created by bhaskar.gangabathina on 23-12-2016.
 */

public class ArticleListEvent {

    private List<Article> articles;

    public ArticleListEvent(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticleList() {
        return articles;
    }

    public void setArticle(List<Article> article) {
        this.articles = articles;
    }
}
