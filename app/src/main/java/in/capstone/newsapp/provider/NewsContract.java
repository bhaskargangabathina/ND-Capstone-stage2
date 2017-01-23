package in.capstone.newsapp.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by bhaskar.gangabathina on 23-01-2017.
 */

public class NewsContract {

    public static final String DB_NAME = "in.capstone.newsapp";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "notifications";
    public static final String AUTHORITY = "in.capstone.newsapp";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
    public static final int ALL_NOTIFICATIONS = 1;
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/news.notifications/" + TABLE;

    public class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String KEY_NOTIFICATION_MSG = "message";
        public static final String KEY_NOTIFICATION_URL = "url";
        public static final String KEY_NOTIFICATION_IMG = "message_img";
        public static final String KEY_NOTIFICATION_TIME = "created_at";
    }
}
