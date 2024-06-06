package nethical.digipaws.utils;
import android.content.Context;
import android.content.SharedPreferences;

public class CoinManager {
    
    public static final int DEFAULT_COUNT = 0;
    
    public static int getCoinCount(Context context){
       SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_COIN_DATA_FILE,
		Context.MODE_PRIVATE);
        return sharedPreferences.getInt(DigiConstants.PREF_COIN_KEY,DEFAULT_COUNT);
    }
    
    public static void resetCoinCount(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_COIN_DATA_FILE,
		Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(DigiConstants.PREF_COIN_KEY,DEFAULT_COUNT).apply();
    }
    
    public static void incrementCoin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_COIN_DATA_FILE,
		Context.MODE_PRIVATE);
        int crnt = sharedPreferences.getInt(DigiConstants.PREF_COIN_KEY,DEFAULT_COUNT);
        sharedPreferences.edit().putInt(DigiConstants.PREF_COIN_KEY,crnt+1).apply();
    }
    
    public static boolean decrementCoin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_COIN_DATA_FILE,
		Context.MODE_PRIVATE);
        int crnt = sharedPreferences.getInt(DigiConstants.PREF_COIN_KEY,DEFAULT_COUNT);
        if(crnt == 0){
            return false;
        }
        sharedPreferences.edit().putInt(DigiConstants.PREF_COIN_KEY,crnt-1).apply();
        return true;
    }
    
    public static void incrementCoinsBy(Context context,int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_COIN_DATA_FILE,
		Context.MODE_PRIVATE);
        int crnt = sharedPreferences.getInt(DigiConstants.PREF_COIN_KEY,DEFAULT_COUNT);
        sharedPreferences.edit().putInt(DigiConstants.PREF_COIN_KEY,crnt+value).apply();
    }
    
}
