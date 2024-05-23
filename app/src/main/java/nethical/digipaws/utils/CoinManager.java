package nethical.digipaws.utils;
import android.content.Context;
import android.content.SharedPreferences;

public class CoinManager {
    
    public static int getCoinCount(Context context){
       SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_COIN_DATA_FILE,
		Context.MODE_PRIVATE);
        return sharedPreferences.getInt(DigiConstants.PREF_COIN_KEY,69);
    }
    
    public static void resetCoinCount(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_COIN_DATA_FILE,
		Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(DigiConstants.PREF_COIN_KEY,0);
    }
    
    public static void incrementCoins(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_COIN_DATA_FILE,
		Context.MODE_PRIVATE);
        int crnt = sharedPreferences.getInt(DigiConstants.PREF_COIN_KEY,69);
        sharedPreferences.edit().putInt(DigiConstants.PREF_COIN_KEY,crnt++).apply();
    }
    
    public static void decrementCoin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_COIN_DATA_FILE,
		Context.MODE_PRIVATE);
        int crnt = sharedPreferences.getInt(DigiConstants.PREF_COIN_KEY,69);
        sharedPreferences.edit().putInt(DigiConstants.PREF_COIN_KEY,crnt--).apply();
    }
    
    public static void incrementCoinsBy(Context context,int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_COIN_DATA_FILE,
		Context.MODE_PRIVATE);
        int crnt = sharedPreferences.getInt(DigiConstants.PREF_COIN_KEY,69);
        sharedPreferences.edit().putInt(DigiConstants.PREF_COIN_KEY,crnt+value).apply();
    }
    
}
