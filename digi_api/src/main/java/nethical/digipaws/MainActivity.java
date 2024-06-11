package nethical.digipaws.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import nethical.digipaws.AppConstants;
import nethical.digipaws.api.databinding.ActivityMainBinding;
import android.content.ComponentName;

public class MainActivity extends AppCompatActivity {
    
    private ActivityMainBinding binding;
    
    private static final int PERMISSION_REQUEST_CODE = 100;
    
    private Button btnRequestPermission;
    private Button btnIncrementCoin;
    private TextView coinCountInfo;
    
    private ContentResolver contentResolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate and get instance of binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // set content view to binding's root
        setContentView(binding.getRoot());
        
        btnRequestPermission = findViewById(R.id.permission);
        btnIncrementCoin = findViewById(R.id.inc_coin);
        
        coinCountInfo = findViewById(R.id.coin_count);
        TextView appModeInfo = findViewById(R.id.mode);
        TextView focusQuestInfo = findViewById(R.id.focus_quest);
        
        contentResolver = getContentResolver();

        coinCountInfo.setText("Aura Coin: " + String.valueOf(getCoinCount()));
         switch(getAppMode()){
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
        
        focusQuestInfo.setText("Is Focus Mode Running: "+ String.valueOf(isFocusQuestRunning()));
        
        btnRequestPermission.setOnClickListener((v)->{
                askPermission();
        });
        
        
        btnIncrementCoin.setOnClickListener((v)->{
            incrementCoins();
        });
        
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, do your work
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT);
                
            } 
        }
    }
    
    private void incrementCoins() {
     try{
        ContentResolver contentResolver = getContentResolver();

        // Build the content URI for updating the coin count
        Uri updateUri = Uri.parse( "content://" + AppConstants.PROVIDER_AUTHORITY + "/"); 
        
        // Call update on the ContentResolver with empty ContentValues (no specific data)
        contentResolver.update(updateUri, new ContentValues(), null, null);
        coinCountInfo.setText("Aura Coin: " + String.valueOf(getCoinCount()));
            
        
     }catch(SecurityException e){
         Toast.makeText(this,"Missing permisions: " + e.toString(),Toast.LENGTH_SHORT).show();
     }
        
  }
    
    
    private void askPermission(){
        if (ContextCompat.checkSelfPermission(this, "nethical.digipaws.permission.API_V0")
                != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(this,
                    new String[]{AppConstants.PERMISSION_MANAGE_QUESTS}, PERMISSION_REQUEST_CODE);
            }
    }
    
    private int getCoinCount(){
       Cursor cursor = contentResolver.query(AppConstants.CONTENT_URI_COIN,null,null,null);
        if (cursor != null && cursor.moveToFirst()) {
            int coinCount = cursor.getInt(cursor.getColumnIndex("count"));
            return coinCount;
        }else{
           Toast.makeText(this,"Digipaws not installed",Toast.LENGTH_LONG).show();
            return 0;
        }
    }
    
    private int getAppMode(){
       Cursor cursor = contentResolver.query(AppConstants.CONTENT_URI_MODE,null,null,null);
        if (cursor != null && cursor.moveToFirst()) {
            int mode = cursor.getInt(cursor.getColumnIndex("mode"));
            return mode;
        }else{
           Toast.makeText(this,"Digipaws not installed",Toast.LENGTH_LONG).show();
            return AppConstants.DIFFICULTY_LEVEL_NORMAL;
        }
    }
    
    private boolean isFocusQuestRunning(){
      Cursor cursor = contentResolver.query(AppConstants.CONTENT_URI_FOCUS_QUEST,null,null,null);
        if (cursor != null && cursor.moveToFirst()) {
            int isfocus = cursor.getInt(cursor.getColumnIndex("focus"));
            return isfocus != 0;
        }else{
            Toast.makeText(this,"Digipaws not installed",Toast.LENGTH_LONG).show();
            return false;
        }
    }
    
}
