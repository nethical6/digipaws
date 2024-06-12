# Digipaws API

The Digipaws API allows querying and managing quest-related data by other applications. This includes operations like querying the coin count, current app mode, focus mode status, and managing cooldown for incrementing coins.
 
## Guidelines 
1. The API allows incrementing 1 coin per hour per package. This was implemented to prevent fake coin increments.
2. Coins are granted only when a user performs something productive. If any apps are found to be faking coin counts, they will be added to the list of blocklists, preventing API usage.
3. Remember that 1 coin grants 20 minutes of leisure time. Hence, refrain from granting coins for partial and easy tasks. For example, a habit tracker app may grant 1 coin after all the tasks for the day have been completed, or an educational platform may grant 1 coin after a user spends 1 hour reading or watching a video.
5. If you want your app to bypass coin increment limitations, please contact the project owner on Discord and explain your idea to enter the whitelist.


## How To Use
An example program using this API can be found [here](https://github.com/nethical6/digipause/tree/main/digi-api).

### Setup Constants

Digipaws uses a content provider to share quest data with other applications. You can find the URI paths and authority address in [this class](https://github.com/nethical6/digipause/blob/main/digi-api/src/main/java/nethical/digipaws/AppConstants.java).


### Setup Content resolver 
After copying all the constants, the second step is to initialise the contentresolver.

```java
// Initialize ContentResolver for database operations
ContentResolver contentResolver = getContentResolver();
```

### Permissions
Accessing data like coin count or app mode doesn't require explicit permission from the user. However, if your app desires to increment coin counts, you need to explicitly ask for permission to manage quests from the user.

Add the following permission to AndroidManifest.xml:
```xml
<uses-permission android:name="nethical.digipaws.permission.API_V1"/>
```

check and ask permission
```java
    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, "nethical.digipaws.permission.API_V0") != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if it is not granted
            ActivityCompat.requestPermissions(this, new String[]{AppConstants.PERMISSION_MANAGE_QUESTS}, PERMISSION_REQUEST_CODE);
        }
    }
```

### Get Coin Count
```java
    private int getCoinCount() {
        Cursor cursor = contentResolver.query(AppConstants.CONTENT_URI_COIN, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int coinCount = cursor.getInt(cursor.getColumnIndex("count"));
            cursor.close();
            return coinCount;
        } else {
            // Show a message if the Digipaws app is not installed
            Toast.makeText(this, "Digipaws not installed", Toast.LENGTH_LONG).show();
            return 0;
        }
    }
```

### Get User Selected Mode
Digipaws provides three different modes to choose from: Easy, Normal (or Adventure), and Hard. The mode selection is stored in the form of an integer where:
- 0 = Easy Mode
- 1 = Normal (or Adventure) Mode
- 2 = Hard Mode

```java
    private int getAppMode() {
        Cursor cursor = contentResolver.query(AppConstants.CONTENT_URI_MODE, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int mode = cursor.getInt(cursor.getColumnIndex("mode"));
            cursor.close();
            return mode;
        } else {
            // Show a message if the Digipaws app is not installed
            Toast.makeText(this, "Digipaws not installed", Toast.LENGTH_LONG).show();
            return AppConstants.DIFFICULTY_LEVEL_NORMAL;
        }
    }

```

### Check if focus quest is running 
Some apps have permission to run even when the focus quest is active. To add your app to this list, you need to open a pull request explaining your app and adding it to [this list](https://github.com/nethical6/digipause/blob/d64ef68253804a447ca2f81125ee1fdaf6987d8d/app/src/main/java/nethical/digipaws/data/BlockerData.java#L18).


```java
    private boolean isFocusQuestRunning() {
        Cursor cursor = contentResolver.query(AppConstants.CONTENT_URI_FOCUS_QUEST, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int isFocus = cursor.getInt(cursor.getColumnIndex("focus"));
            cursor.close();
            return isFocus != 0;
        } else {
            // Show a message if the Digipaws app is not installed
            Toast.makeText(this, "Digipaws not installed", Toast.LENGTH_LONG).show();
            return false;
        }
    }
```

### Check if cooldown to incrementing coins is over
As described earlier, an app can increment only 1 coin per hour. The API provides methods to check both the remaining time and whether the cooldown is active.

```java
    private boolean isCoinInCooldownOver() {
        Cursor cursor = contentResolver.query(AppConstants.CONTENT_URI_UPDATE_DATA_DELAY, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int isDelayActive = cursor.getInt(cursor.getColumnIndex("is_active"));
            if (isDelayActive == 0) {
                // Show remaining cooldown time if delay is active
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
```

### Incrementing Coins
After checking if the cooldown is over or not, one can implement the coin incrementation process.

> [!CAUTION]  
> If an app attempts to increment coins without permission, a security exception is thrown.
> Querying data does not require any permission, however, updating data requires explicit permission.

```java
    private void incrementCoins() {
        try {
            if (!isCoinInCooldownOver()) {
                return;
            }

            // Update the coin count
            contentResolver.update(AppConstants.CONTENT_URI_COIN, new ContentValues(), null, null);
        } catch (SecurityException e) {
            // Handle the exception if permission is missing
            Toast.makeText(this, "Missing permissions: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
```
