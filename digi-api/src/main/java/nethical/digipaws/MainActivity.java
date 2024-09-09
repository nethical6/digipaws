package nethical.digipaws.api;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import nethical.digipaws.AppConstants;
import nethical.digipaws.api.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 100;

    private TextView isAddedToList;
    private TextView coinCountInfo;

    private ContentResolver contentResolver;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize UI components
        Button btnRequestPermission = findViewById(R.id.permission);
        Button btnIncrementCoin = findViewById(R.id.inc_coin);
        coinCountInfo = findViewById(R.id.coin_count);
        Button btnAddToList = findViewById(R.id.addToQuests);
        
        TextView appModeInfo = findViewById(R.id.mode);
        TextView focusQuestInfo = findViewById(R.id.focus_quest);
        isAddedToList = findViewById(R.id.is_added_to_list);
        // Initialize ContentResolver for database operations
        contentResolver = getContentResolver();

        // get coin count
        coinCountInfo.setText("Aura Coin: " + getCoinCount());

        // get current app mode
        switch (getAppMode()) {
            case AppConstants.DIFFICULTY_LEVEL_EASY:
                appModeInfo.setText("Current Mode Config: Easy Mode (0)");
                break;
            case AppConstants.DIFFICULTY_LEVEL_NORMAL:
                appModeInfo.setText("Current Mode Config: Adventure/Normal Mode (1)");
                break;
            case AppConstants.DIFFICULTY_LEVEL_EXTREME:
                appModeInfo.setText("Current Mode Config: Hard Mode (2)");
                break;
        }

        // check if focus quest is running
        focusQuestInfo.setText("Is Focus Mode Running: " + isFocusQuestRunning());

        isAddedToList.setText("is added to quest list: "+ checkIfAddedToList());
        // The app needs to explicitly ask for user permission to manage quests in order to increment the coin count. 
        // Retrieving other data like available coins doesn't require explicit permissions.
       btnRequestPermission.setOnClickListener(v -> askPermission());
        
        btnIncrementCoin.setOnClickListener(v -> incrementCoins());
        
        btnAddToList.setOnClickListener(v -> addToQuestList());
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up the binding reference
        binding = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, inform the user
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void incrementCoins() {
        try {
            if (!isCoinInCooldownOver()) {
                return;
            }

            // Update the coin count
            contentResolver.update(AppConstants.CONTENT_URI_COIN, new ContentValues(), null, null);

            // Refresh the coin count display
            coinCountInfo.setText("Aura Coin: " + getCoinCount());
        } catch (SecurityException e) {
            // Handle the exception if permission is missing
            Toast.makeText(this, "Missing permissions: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("SetTextI18n")
    private void addToQuestList() {
        try {

            // Update the coin count
            contentResolver.update(AppConstants.CONTENT_URI_ADD_TO_QUESTS, new ContentValues(), null, null);

            // Refresh the coin count display
            isAddedToList.setText("is added to quest list: " + checkIfAddedToList());
        } catch (SecurityException e) {
            // Handle the exception if permission is missing
            Toast.makeText(this, "Missing permissions: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkIfAddedToList(){
        Cursor cursor = contentResolver.query(AppConstants.CONTENT_URI_IS_ADDED_TO_QUESTS, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range")
            int isAdded = cursor.getInt(cursor.getColumnIndex("is_added"));
            cursor.close();
            return isAdded != 0;
        } else {
            // Show a message if the Digipaws app is not installed
            Toast.makeText(this, "Digipaws not installed", Toast.LENGTH_LONG).show();
            return false;
        }
    }
    

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, "nethical.digipaws.permission.API_V0") != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if it is not granted
            ActivityCompat.requestPermissions(this, new String[]{AppConstants.PERMISSION_MANAGE_QUESTS}, PERMISSION_REQUEST_CODE);
        }
    }

    private int getCoinCount() {
        Cursor cursor = contentResolver.query(AppConstants.CONTENT_URI_COIN, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range")
            int coinCount = cursor.getInt(cursor.getColumnIndex("count"));
            cursor.close();
            return coinCount;
        } else {
            // Show a message if the Digipaws app is not installed
            Toast.makeText(this, "Digipaws not installed", Toast.LENGTH_LONG).show();
            return 0;
        }
    }

    private int getAppMode() {
        Cursor cursor = contentResolver.query(AppConstants.CONTENT_URI_MODE, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range")
            int mode = cursor.getInt(cursor.getColumnIndex("mode"));
            cursor.close();
            return mode;
        } else {
            // Show a message if the Digipaws app is not installed
            Toast.makeText(this, "Digipaws not installed", Toast.LENGTH_LONG).show();
            return AppConstants.DIFFICULTY_LEVEL_NORMAL;
        }
    }

    private boolean isFocusQuestRunning() {
        Cursor cursor = contentResolver.query(AppConstants.CONTENT_URI_FOCUS_QUEST, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range")
            int isFocus = cursor.getInt(cursor.getColumnIndex("focus"));
            cursor.close();
            return isFocus != 0;
        } else {
            // Show a message if the Digipaws app is not installed
            Toast.makeText(this, "Digipaws not installed", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean isCoinInCooldownOver() {
        Cursor cursor = contentResolver.query(AppConstants.CONTENT_URI_UPDATE_DATA_DELAY, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range")
            int isDelayActive = cursor.getInt(cursor.getColumnIndex("is_active"));
            if (isDelayActive == 0) {
                // Show remaining cooldown time if delay is active
                @SuppressLint("Range")
                long remainingTime = cursor.getLong(cursor.getColumnIndex("remaining_time"));
                Toast.makeText(this, remainingTime + " ms remaining before a new coin can be generated.", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            return isDelayActive != 0;
        } else {
            // Show a message if the Digipaws app is not installed
            Toast.makeText(this, "Digipaws not installed", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
