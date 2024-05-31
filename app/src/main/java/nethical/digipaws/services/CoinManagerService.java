package nethical.digipaws.services;

import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.R;

public class CoinManagerService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
    @Override
    public int onStartCommand(Intent intent, int arg1, int arg2) {
        if(intent.getAction()==null){
            return START_NOT_STICKY;
        }
        
        switch(intent.getAction()){
            case DigiConstants.COIN_MANAGER_DECREMENT:
                CoinManager.decrementCoin(this);
                sendNotification(DigiConstants.COIN_MANAGER_DECREMENT,intent);
                break;
            case DigiConstants.COIN_MANAGER_INCREMENT:
                CoinManager.incrementCoin(this);
                sendNotification(DigiConstants.COIN_MANAGER_INCREMENT,intent);
                break;
        }
        
        return START_NOT_STICKY;
    }
    
    private String getMessage(String type,Intent intent){
        String msg = intent.getStringExtra(DigiConstants.COIN_MANAGER_NOTIF_DESC);
        if(msg == null || msg == ""){
            switch(type){
            case DigiConstants.COIN_MANAGER_INCREMENT:
                return "You earned 1 DigiCoin";
            case DigiConstants.COIN_MANAGER_DECREMENT:
                return "You spent 1 DigiCoin";
            }
        }
        return msg;
    }
    
    public void sendNotification(String type,Intent intent){
        
        switch(type){
            case DigiConstants.COIN_MANAGER_INCREMENT:
                DigiUtils.sendNotification(this,"Quest Completed!! ",getMessage(type,intent),R.drawable.swords);
                break;
            case DigiConstants.COIN_MANAGER_DECREMENT:
                DigiUtils.sendNotification(this,"Digicoin",getMessage(type,intent),R.drawable.swords);
                break;
        }
    }
    
}
