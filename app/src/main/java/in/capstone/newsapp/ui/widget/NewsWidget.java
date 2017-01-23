package in.capstone.newsapp.ui.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import in.capstone.newsapp.R;
import in.capstone.newsapp.api.APIClient;
import in.capstone.newsapp.models.Article;
import in.capstone.newsapp.models.NewsArticlesListResponse;
import in.capstone.newsapp.ui.activity.SourcesActivity;
import in.capstone.newsapp.utils.Constants;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bhaskar.gangabathina on 08-01-2017.
 */

public class NewsWidget extends AppWidgetProvider {

    private List<Article> mArticles = new ArrayList<>();
    private Subscription subscription;
    private int position = 0;

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        if(mArticles.size() == 0) {
            initData(context, views);
        }
        setData(context, views);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void setData(Context context, @NonNull final RemoteViews views) {
        views.setViewVisibility(R.id.progress, View.GONE);
        if(mArticles.size() > 0){
            views.setTextViewText(R.id.widget_title, mArticles.get(position).getTitle());
            views.setTextViewText(R.id.widget_source, mArticles.get(position).getAuthor());
            views.setTextViewText(R.id.widget_date, mArticles.get(position).getPublishedAt());
        }
            Intent intent = new Intent(context, SourcesActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget_layout_main, pendingIntent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            Log.v("BHASKAR", "onUpdate() news widget");
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void initData(final Context context, final RemoteViews view) {

        view.setViewVisibility(R.id.progress, View.VISIBLE);
        subscription = APIClient.getInstance()
                .getArticles("bbc-news", Constants.TOP_ARTICLES, Constants.NEWS_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsArticlesListResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(NewsArticlesListResponse sources) {

                        mArticles.clear();
                        List<Article> articles = sources.getArticles();
                        mArticles.addAll(articles);
                        Log.v("BHASKAR", "article list size="+mArticles.size());
                        updateDataWidget(context);
                    }
                });


    }


    private void updateDataWidget(Context context){
        Intent intent = new Intent(context,NewsWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
//        int[] ids = {R.id.widget_title, R.id.widget_source, R.id.widget_date, R.id.progress};
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        context.sendBroadcast(intent);
    }
}
