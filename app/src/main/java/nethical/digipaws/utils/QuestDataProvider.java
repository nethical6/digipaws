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
import android.os.SystemClock;
import nethical.digipaws.R;

public class QuestDataProvider extends ContentProvider {

    private SharedPreferences preferences;
    private Context context;
    
    
    public final Uri CONTENT_URI_COIN =
            Uri.parse("content://" + DigiConstants.PROVIDER_AUTHORITY + "/coin");
    public final Uri CONTENT_URI_MODE =
            Uri.parse("content://" + DigiConstants.PROVIDER_AUTHORITY + "/mode");
    public final Uri CONTENT_URI_FOCUS_MODE =
            Uri.parse("content://" + DigiConstants.PROVIDER_AUTHORITY + "/focus");
    public final Uri CONTENT_URI_UPDATE_DATA_DELAY =
            Uri.parse("content://" + DigiConstants.PROVIDER_AUTHORITY + "/get_delay_details");
    public final Uri CONTENT_URI_ADD_TO_QUESTS =
            Uri.parse("content://" + DigiConstants.PROVIDER_AUTHORITY + "/add_to_quests");
    public final Uri CONTENT_URI_IS_QUEST =
            Uri.parse("content://" + DigiConstants.PROVIDER_AUTHORITY + "/is_quest");
    
    
    public final int ERROR_CODE_UNKNOWN = 0;
    
    public final int SUCCESS_CODE = 1;
    public final int ERROR_CODE_COOLDOWN_ACTIVE = 2;
    
    @Override
    public boolean onCreate() {
        preferences = getContext().getSharedPreferences(DigiConstants.PREF_QUEST_MANAGING_PERM, Context.MODE_PRIVATE);
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
        
        // updating values requires explict user permission
       int permissionCheck =
            getContext().checkCallingOrSelfPermission(DigiConstants.PERMISSION_MANAGE_QUEST);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(
                    "Permission Missing: " + DigiConstants.PERMISSION_MANAGE_QUEST);
        }
        
        if(uri.equals(CONTENT_URI_COIN)){
            if(isCooldownOver(getCallingPackage())){
                DigiUtils.sendNotification(context, "Coin Earned", "You earned 1 Aura coin", R.drawable.swords);
                CoinManager.incrementCoin(context);
                startCooldown(getCallingPackage());
                return SUCCESS_CODE;
            } else {
                return ERROR_CODE_COOLDOWN_ACTIVE;
            }
        }
        if(uri.equals(CONTENT_URI_ADD_TO_QUESTS)){
            InstalledQuestsManager iqm = new InstalledQuestsManager(getContext());
            iqm.append(getCallingPackage());
        }
        return ERROR_CODE_UNKNOWN;
        
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Not allowed to delete coin count
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
            int mode = SurvivalModeManager.isSurvivalModeActive(getContext()) ? 1:0;
            cursor.addRow(new Object[] { mode });
            return cursor;
        }
        if (uri.equals(CONTENT_URI_UPDATE_DATA_DELAY)) {
            // Create a dummy cursor with the coin count
            MatrixCursor cursor = new MatrixCursor(new String[] { "is_active","remaining_time"},1);
            int isActive = isCooldownOver(getCallingPackage()) ? 1:0;
            long startTime = getCooldownStartTime(getCallingPackage());
            long remainingTime = DigiConstants.API_COIN_INC_COOLDOWN - (SystemClock.uptimeMillis() - startTime);
            cursor.addRow(new Object[] { isActive,remainingTime});
            return cursor;
        }
        if (uri.equals(CONTENT_URI_IS_QUEST)) {
            // Create a dummy cursor with the coin count
            MatrixCursor cursor = new MatrixCursor(new String[] { "is_installed"},1);
            InstalledQuestsManager iqm = new InstalledQuestsManager(getContext());
            int isAdded = iqm.isAdded(getCallingPackage()) ? 1:0;
            cursor.addRow(new Object[] { isAdded});
            return cursor;
        }
        return null;
    }

    private void startCooldown(String packageName){
        long currentTime = SystemClock.uptimeMillis();
        preferences.edit().putLong(packageName,currentTime).apply();
    }
    
    private long getCooldownStartTime(String packageName){
        return preferences.getLong(packageName,0L);
    }
    
    private boolean isCooldownOver(String packageName){
        return DelayManager.isDelayOver(getCooldownStartTime(packageName),DigiConstants.API_COIN_INC_COOLDOWN);
    }
    
}
