package in.capstone.newsapp.ui.views;

import java.util.List;

import in.capstone.newsapp.models.Source;

/**
 * Created by bhaskar.gangabathina on 22-12-2016.
 */

public interface SourceGridView {

    public void showLoading();

    public void hideLoading();

    public void showSources(List<Source> sources);

    public void showError(String message, int type);

    public void hideError();

}
