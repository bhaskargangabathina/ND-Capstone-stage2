package in.capstone.newsapp.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import rx.Subscription;


/**
 * Created by bhaskar.gangabathina on 03-01-2017.
 */


public class FBInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FBInstanceIDService";
    Subscription subscription;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh: " + refreshedToken);
        saveToken(refreshedToken);
    }

    private void saveToken(@NonNull String token) {
        Log.d(TAG, "TOKEN : " + token);
    }
}
