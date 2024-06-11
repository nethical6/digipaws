
package nethical.digipaws.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import nethical.digipaws.api.databinding.ActivityMainBinding;
import android.content.ComponentName;

public class MainActivity extends AppCompatActivity {
    
    private ActivityMainBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private final String PERMISSION = "nethical.digipaws.permission.API_V1";
    private final String CONTENT_AUTHORITY = "nethical.digipaws.questprovider";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate and get instance of binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // set content view to binding's root
        setContentView(binding.getRoot());
        
        Button requestPermissionBtn = findViewById(R.id.permission);
        Button incrementCoinBtn = findViewById(R.id.inc_coin);
        
        requestPermissionBtn.setOnClickListener((v)->{
            if (ContextCompat.checkSelfPermission(this, "nethical.digipaws.permission.API_V0")
                != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(this,
                    new String[]{"nethical.digipaws.permission.API_V1"}, PERMISSION_REQUEST_CODE);
            }
        });
        
        
        incrementCoinBtn.setOnClickListener((v)->{
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
            } else {
                // Permission was denied, handle the denial
            }
        }
    }
    
    private void incrementCoins() {
     try{
        ContentResolver contentResolver = getContentResolver();

        // Build the content URI for updating the coin count
        Uri updateUri = Uri.parse( "content://" + CONTENT_AUTHORITY + "/"); 
        
        // Call update on the ContentResolver with empty ContentValues (no specific data)
        int a = contentResolver.update(updateUri, new ContentValues(), null, null);
        
     }catch(SecurityException e){
         Toast.makeText(this,"Missing permisions",Toast.LENGTH_SHORT).show();
     }
        
    }
}
