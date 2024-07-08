package nethical.digipaws.utils;

import android.content.Context;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.util.GeoPoint;

public class LocationManager {
	
	private Context context;
	private LocationRequest locationRequest;
	private LocationCallback locationCallback;
	private LocationListener listener;
	public boolean isRunning = false;
	
	public LocationManager(Context context) {
		this.context = context;
		createLocationRequest();
		createLocationCallback();
	}
	
	public void setLiveLocationListener(LocationListener listener) {
		this.listener = listener;
	}
	
	private void createLocationRequest() {
		locationRequest = LocationRequest.create();
		locationRequest.setInterval(5000); // Update interval in milliseconds
		locationRequest.setFastestInterval(5000); // Fastest update interval in milliseconds
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}
	
	private void createLocationCallback() {
		locationCallback =
		new LocationCallback() {
			@Override
			public void onLocationResult(
			com.google.android.gms.location.LocationResult locationResult) {
				if (locationResult == null) {
					return;
				}
				Location lastKnownLocation = locationResult.getLastLocation();
				if (listener != null) {
					listener.onLocationChanged(
					lastKnownLocation); // Call listener with updated location
				}
			}
		};
        
	}
    
    
	
	public void startLocationUpdates() {
		isRunning = true;
		LocationServices.getFusedLocationProviderClient(context)
		.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
	}
	
	public void stopLocationUpdates() {
        if(isRunning==false){return;}
		isRunning = false;
		LocationServices.getFusedLocationProviderClient(context)
		.removeLocationUpdates(locationCallback);
	}
	
	public void getLastLocation(LocationListener listener) {
		LocationServices.getFusedLocationProviderClient(context)
		.getLastLocation()
		.addOnSuccessListener(
		location -> {
			if (location != null) {
				listener.onLocationChanged(location);
			}
		});
	}
	
	public float getDistanceBetweenLocations(Location currentLocation, Location constantLocation) {
		
		if (currentLocation != null) {
			return currentLocation.distanceTo(constantLocation);
			} else {
			return -1; // Indicate failure to get current location
		}
	}
	
	public float getDistanceBetweenLocations(GeoPoint currentLocation, GeoPoint constantLocation) {
		Location crnt_loc = new Location("n");
		crnt_loc.setLatitude(currentLocation.getLatitude());
		crnt_loc.setLongitude(currentLocation.getLongitude());
		
		Location cnst_loc = new Location("g");
		cnst_loc.setLatitude(constantLocation.getLatitude());
		cnst_loc.setLongitude(constantLocation.getLongitude());
		return crnt_loc.distanceTo(cnst_loc);
	}
	
	private Location getLastKnownLocation() {
		try {
			return LocationServices.getFusedLocationProviderClient(context)
			.getLastLocation()
			.getResult();
			} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public interface LocationListener {
		void onLocationChanged(Location location);
	}
}