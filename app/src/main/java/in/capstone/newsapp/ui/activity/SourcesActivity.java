package in.capstone.newsapp.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.capstone.newsapp.R;
import in.capstone.newsapp.ui.adapters.NewsSourcesGridAdapter;
import in.capstone.newsapp.NewsApp;
import in.capstone.newsapp.models.Source;
import in.capstone.newsapp.presenter.NewsSourcesPresenter;
import in.capstone.newsapp.utils.Constants;
import in.capstone.newsapp.ui.views.ErrorBuilder;
import in.capstone.newsapp.ui.views.ErrorClickListener;
import in.capstone.newsapp.ui.views.SourceGridView;

public class SourcesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewsSourcesGridAdapter.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        SourceGridView {

    private static final String TAG = "MainActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sources_recycler)
    RecyclerView newsSorcesRecyclerView;
    @BindView(R.id.loadingProgress)
    ProgressBar loadingProgress;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    private NewsSourcesGridAdapter newsSourcesGridAdapter;
    private ArrayList<Source> newsSourcesList = new ArrayList<>();
    private ActionBarDrawerToggle toggle;
    private FirebaseAuth mAuth;

    private GoogleApiClient mGoogleApiClient;
    private NewsSourcesPresenter newsSourcesPresenter;
    private ErrorBuilder errorBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);
        ButterKnife.bind(this);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        setSupportActionBar(toolbar);

        if (NewsApp.getAppInstance().getLoginStatus()) {
            initFirebaseAuthentication();
        }

        initHeaderViewUi();
        initUi();

        errorBuilder = new ErrorBuilder(this, errorLayout, new ErrorClickListener() {
            @Override
            public void onRetryClicked(View view) {
                if (newsSourcesPresenter != null) {
                    newsSourcesPresenter.destroy();
                    newsSourcesPresenter.start();
                }
            }
        });

        newsSourcesPresenter = new NewsSourcesPresenter(this);
        newsSourcesPresenter.start();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        ;
        if (id == R.id.nav_notifications) {
            intent = new Intent(this, NotificationsActivity.class);
        } else if (id == R.id.nav_about) {
            intent = new Intent(this, AboutUsActivity.class);
        } else if (id == R.id.nav_credits) {
            intent = new Intent(this, CreditsActivity.class);
        } else if (id == R.id.nav_logout) {
            signOut();
        } else if (id == R.id.nav_rateus) {
            rateUsonPlaystore();
        }

        if (intent != null) {
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initFirebaseAuthentication() {
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    @Override
    protected void onDestroy() {
        if (newsSourcesPresenter != null) {
            newsSourcesPresenter.destroy();
        }
        super.onDestroy();
    }


    private void initUi() {
        newsSourcesGridAdapter = new NewsSourcesGridAdapter(this, newsSourcesList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        newsSorcesRecyclerView.setLayoutManager(layoutManager);
        newsSorcesRecyclerView.setAdapter(newsSourcesGridAdapter);
        newsSourcesGridAdapter.setOnClickListener(this);
    }

    private void initHeaderViewUi() {
        View headerView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) headerView.findViewById(R.id.userName);
        TextView nav_email = (TextView) headerView.findViewById(R.id.userMail);
        CircularImageView nav_userImage = (CircularImageView) headerView.findViewById(R.id.userProfileImage);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            nav_user.setText(user.getDisplayName());
            nav_email.setText(user.getEmail());
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .crossFade()
                    .into(nav_userImage);
        }

    }

    @Override
    public void onClick(int position) {
        String sourceId = newsSourcesList.get(position).getId();
        Intent articleList = new Intent(SourcesActivity.this, ArticlesActivity.class);
        articleList.putExtra(Constants.SOURCE_ID, sourceId);
        articleList.putExtra(Constants.SOURCE_NAME, newsSourcesList.get(position).getName());
        startActivity(articleList);
    }

    private void signOut() {
        if (mAuth != null && mGoogleApiClient != null) {
            mAuth.signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            NewsApp.getAppInstance().setLoginStatus(false);
                            startActivity(new Intent(SourcesActivity.this, SplashScreenActivity.class));
                            finish();
                        }
                    });

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void showLoading() {
        loadingProgress.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoading() {
        loadingProgress.setVisibility(View.GONE);
    }

    @Override
    public void showSources(List<Source> sources) {
        newsSourcesList.clear();
        newsSourcesList.addAll(sources);
        newsSourcesGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message, int type) {
        errorBuilder.showError(message, type, true);
    }

    @Override
    public void hideError() {
        errorBuilder.hideErrorLayout();
    }

    private void rateUsonPlaystore() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.msg_playstore_app_not_found, Toast.LENGTH_LONG).show();
        }
    }
}
