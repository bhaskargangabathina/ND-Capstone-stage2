package in.capstone.newsapp.api;

import in.capstone.newsapp.models.NewsArticlesListResponse;
import in.capstone.newsapp.models.SourcesResponce;
import in.capstone.newsapp.utils.Constants;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by bhaskar.gangabathina
 */

public interface APIService {

    @GET("sources")
    Observable<SourcesResponce> getNewsSources(@Query(Constants.KEY_LANGUAGE) String language, @Query(Constants.KEY_COUNTRY)
            String country);

    //https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=fdf9c53649454ac8bedb495857725aae
    @GET("articles")
    Observable<NewsArticlesListResponse> getNewsArticles(@Query(Constants.KEY_SOURCE) String source, @Query
            (Constants.KEY_SORT_BY) String sortBy, @Query(Constants.KEY_API_KEY) String apiKey);

}
