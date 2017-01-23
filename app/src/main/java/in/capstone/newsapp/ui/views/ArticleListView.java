package in.capstone.newsapp.ui.views;

import java.util.List;

import in.capstone.newsapp.models.Article;

/**
 * Created by bhaskar.gangabathina on 22-12-2016.
 */

public interface ArticleListView {

    public void showLoading();

    public void hideLoading();

    public void showArticles(List<Article> articles);

    public void showError(String message, int type);

    public void hideError();

}
