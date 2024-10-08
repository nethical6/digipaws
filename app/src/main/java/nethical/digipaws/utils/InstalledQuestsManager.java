package nethical.digipaws.utils;

import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;
import android.content.Context;
import java.util.Set;
import java.util.HashSet;

public class InstalledQuestsManager {

    private final SharedPreferences sharedPreference;
    
    public InstalledQuestsManager(Context context){
        sharedPreference = context.getSharedPreferences(DigiConstants.PREF_PERMISSION_USERS_FILE,
				Context.MODE_PRIVATE);
    }
    public void append(String packageName) {
    	List<String> apiUsers = getList();
        
            if(apiUsers.contains(packageName)){
                return;
            }
        
    apiUsers.add(packageName);
        save(apiUsers);
    }
    
    public List<String> getList() {
		return new ArrayList<>(sharedPreference.getStringSet(DigiConstants.PREF_PERMISSION_USERS_KEY, new HashSet<>()));
    }
    public void remove(String packageName){
        List<String> apiUsers = getList();
    	for (String value : apiUsers) {
            apiUsers.remove(value);
		}
        save(apiUsers);
    }
    private void save(List<String> users){
        SharedPreferences.Editor editor = sharedPreference.edit();
		editor.putStringSet(DigiConstants.PREF_PERMISSION_USERS_KEY, new HashSet<>(users));
		editor.apply();
    }
    public boolean isAdded(String packageName){
       List<String> apiUsers = getList();
        return apiUsers.contains(packageName);
        
    }
}
