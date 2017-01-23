package in.capstone.newsapp.bus;

import in.capstone.newsapp.models.Article;

/**
 * Created by bhaskar.gangabathina on 23-12-2016.
 */

public class ArticleEvent {

    private Article article;

    public ArticleEvent(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
