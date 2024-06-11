package nethical.digipaws.utils;

import android.content.ContentProvider;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.net.Uri;
import android.content.ContentValues;
import android.database.Cursor;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import nethical.digipaws.R;

public class QuestDataProvider extends ContentProvider {

    private SharedPreferences mSharedPreferences;
    private Context context;
    
    
    public static final Uri CONTENT_URI_COIN =
            Uri.parse("content://" + DigiConstants.PROVIDER_AUTHORITY + "/coin");
    public static final Uri CONTENT_URI_MODE =
            Uri.parse("content://" + DigiConstants.PROVIDER_AUTHORITY + "/mode");
    public static final Uri CONTENT_URI_FOCUS_MODE =
            Uri.parse("content://" + DigiConstants.PROVIDER_AUTHORITY + "/focus");
    
    
    @Override
    public boolean onCreate() {
        mSharedPreferences = getContext().getSharedPreferences("quest_data", Context.MODE_PRIVATE);
        this.context = getContext();
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null; // Not required for simple data types
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Not applicable for retrieving coin count
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int permissionCheck =
                getContext().checkCallingOrSelfPermission(DigiConstants.PERMISSION_MANAGE_QUEST);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(
                    "Requires permission: " + DigiConstants.PERMISSION_MANAGE_QUEST);
        }
        DigiUtils.sendNotification(context, "Coin earned", getCallingPackage(), R.drawable.swords);
        CoinManager.incrementCoin(context);
        return 1;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Not applicable for retrieving coin count
        return 0;
    }

    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
        if (uri.equals(CONTENT_URI_COIN)) {
            // Create a dummy cursor with the coin count
            MatrixCursor cursor = new MatrixCursor(new String[] { "count" }, 1);
            cursor.addRow(new Object[] { CoinManager.getCoinCount(context) });
            return cursor;
        }
        if (uri.equals(CONTENT_URI_MODE)) {
            // Create a dummy cursor with the coin count
            MatrixCursor cursor = new MatrixCursor(new String[] { "mode" },1);
            SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_APP_CONFIG,Context.MODE_PRIVATE);
            int mode = sharedPreferences.getInt(DigiConstants.PREF_MODE,DigiConstants.DIFFICULTY_LEVEL_EASY);
            cursor.addRow(new Object[] { mode });
            return cursor;
        }
        if (uri.equals(CONTENT_URI_FOCUS_MODE)) {
            // Create a dummy cursor with the coin count
            MatrixCursor cursor = new MatrixCursor(new String[] { "focus" },1);
            cursor.addRow(new Object[] { SurvivalModeManager.isSurvivalModeActive(getContext()) });
            return cursor;
        }
        
        
        return null;
    }

    
}
