package nethical.digipaws.fragments.quests;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import nethical.digipaws.R;
import nethical.digipaws.fragments.dialogs.LoadingDialog;
import nethical.digipaws.services.LocationTrackerService;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import nethical.digipaws.utils.LocationManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.MapView;


public class MarathonQuest extends Fragment {
	
	private MapView mapView;
    private Button startQuest;
    
    private boolean isRunning;
    
    private SharedPreferences questPref;
    
	private LocationManager liveLocationTracker = null;
	
    private Location radarLocation = new Location("radar");
    
	private MyLocationNewOverlay mLocationOverlay;
	LoadingDialog loadingDialog = new LoadingDialog("Calculating Location...");
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.marathon_quest_layout, container, false);
		
		mapView = view.findViewById(R.id.map);
        startQuest = view.findViewById(R.id.start_location_quest);
        
		Configuration.getInstance()
		.load(
		requireContext(),
		PreferenceManager.getDefaultSharedPreferences(requireContext()));
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
        
        
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    69); // Use a unique request code
            }

        
        
        if(!DigiUtils.isNotificationAccessEnabled(requireContext())){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    70);
            }
          }
	
		loadingDialog.show(getActivity().getSupportFragmentManager(), "loading_dialog"); // Use a unique tag for the dialog
		
		
		mapView.setTileSource(TileSourceFactory.MAPNIK);
		
		mapView.setMultiTouchControls(true);
		
		Marker myLocation = new Marker(mapView);
		
		mapView.getOverlays().add(myLocation);
		
        questPref = requireContext().getSharedPreferences(DigiConstants.PREF_QUEST_INFO_FILE,
				Context.MODE_PRIVATE);
        
        if(questPref.getBoolean(DigiConstants.PREF_IS_QUEST_RUNNING_KEY,false) && questPref.getString(DigiConstants.PREF_QUEST_ID_KEY,DigiConstants.QUEST_ID_NULL)==DigiConstants.QUEST_ID_MARATHON){
                float latitude = questPref.getFloat(DigiConstants.KEY_RADAR_LATITUDE,0f);
                float longitude = questPref.getFloat(DigiConstants.KEY_RADAR_LONGITUDE,0f);
                radarLocation.setLatitude(latitude);
                radarLocation.setLongitude(longitude);
                
                addCircleTo(latitude,longitude,DigiConstants.RADAR_RADIUS);
                IMapController controller = mapView.getController();
				controller.setZoom(18.0);
                GeoPoint loc = new GeoPoint(latitude,longitude);
				controller.animateTo(loc);
                isRunning= true;
                loadingDialog.dismiss();
                startQuest.setText(R.id.stop);
        } else {
            makeRadar();
        }
        
        
		liveLocationTracker = new LocationManager(requireContext());
		
		
		// updates current location on map
		new Thread(
		() -> {
			liveLocationTracker.setLiveLocationListener(
			new LocationManager.LocationListener() {
				@Override
				public void onLocationChanged(Location location) {
					if (location != null) {
						GeoPoint currentLocation =
						new GeoPoint(
						location.getLatitude(),
						location.getLongitude());
						
						myLocation.setPosition(currentLocation);
						
					}
				}
				
			});
			liveLocationTracker.startLocationUpdates();
		})
		.start();
		
		
		
		
        startQuest.setOnClickListener(v -> {
            if(!isRunning){
                SharedPreferences.Editor editor = questPref.edit();
                editor.putString(DigiConstants.PREF_QUEST_ID_KEY,DigiConstants.QUEST_ID_MARATHON);
                editor.putBoolean(DigiConstants.PREF_IS_QUEST_RUNNING_KEY,true);
                editor.apply();      
                    
                startQuest.setText(R.string.stop);
                isRunning=true;
                Intent serviceIntent = new Intent(requireContext(), LocationTrackerService.class);
                serviceIntent.putExtra(DigiConstants.KEY_RADAR_LATITUDE,radarLocation.getLatitude());
                serviceIntent.putExtra(DigiConstants.KEY_RADAR_LONGITUDE,radarLocation.getLongitude());
                serviceIntent.setAction("START");
                requireContext().startForegroundService(serviceIntent);
            }else{
                startQuest.setText(R.string.start);
                isRunning=false;
                Intent serviceIntent = new Intent(requireContext(), LocationTrackerService.class);
                serviceIntent.putExtra(DigiConstants.KEY_RADAR_LATITUDE,radarLocation.getLatitude());
                serviceIntent.putExtra(DigiConstants.KEY_RADAR_LONGITUDE,radarLocation.getLongitude());
                serviceIntent.setAction("STOP");
                requireContext().startForegroundService(serviceIntent);
            }
        });
	}
	
	
	private void makeRadar(){
		LocationManager locationHelperCircle = new LocationManager(requireContext());
		
		locationHelperCircle.setLiveLocationListener(
		new LocationManager.LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					
					GeoPoint currentLocation =
					new GeoPoint(location.getLatitude(), location.getLongitude());
					
					
					IMapController controller = mapView.getController();
					controller.setZoom(18.0);
					controller.animateTo(currentLocation);
					
					
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();
					double radius = DigiConstants.RADAR_RADIUS; // Radius in meters
					
                    SharedPreferences.Editor editor = questPref.edit();
                        
                    editor.putFloat(DigiConstants.KEY_RADAR_LATITUDE,(float) latitude);
                    editor.putFloat(DigiConstants.KEY_RADAR_LONGITUDE,(float) longitude);
                    editor.putString(DigiConstants.PREF_QUEST_ID_KEY,DigiConstants.QUEST_ID_MARATHON);
                    editor.putBoolean(DigiConstants.PREF_IS_QUEST_RUNNING_KEY,true);
                    editor.apply();    
                       
                        
                    radarLocation.set(location);
					addCircleTo(latitude,longitude,radius);
					loadingDialog.dismiss();
					locationHelperCircle.stopLocationUpdates();
					
				}
			}
		});
		locationHelperCircle.startLocationUpdates();
	}
	
	private void addCircleTo(double latitude,double longitude,double radius){
		Polygon circlePolygon = drawCircle(mapView, latitude, longitude, radius);
		mapView.getOverlays().add(circlePolygon);
	}

	
	public Polygon drawCircle(MapView map, double latitude, double longitude, double radius) {
		
		GeoPoint centerPoint = new GeoPoint(latitude, longitude);
		
		double circleRadius = radius; // in meters
		
		ArrayList<GeoPoint> circlePoints = new ArrayList<>();
		
		// Loop to calculate points
		for (float angle = 0; angle <= 360; angle += 1.0f) {
			GeoPoint newPoint = centerPoint.destinationPoint(circleRadius, angle);
			circlePoints.add(newPoint);
		}
		
		Polygon circlePolygon = new Polygon(map); 
		
		circlePolygon.setPoints(circlePoints);
		
		circlePolygon.setFillColor(Color.argb(64, 255, 0, 0)); 
		circlePolygon.setStrokeColor(Color.TRANSPARENT);
		return circlePolygon;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
		
		if (liveLocationTracker != null && !liveLocationTracker.isRunning) {
			liveLocationTracker.startLocationUpdates();
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		if (liveLocationTracker != null && liveLocationTracker.isRunning) {
			liveLocationTracker.stopLocationUpdates();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		liveLocationTracker.stopLocationUpdates();
		if (liveLocationTracker != null && liveLocationTracker.isRunning) {
			liveLocationTracker.stopLocationUpdates();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
        liveLocationTracker.stopLocationUpdates();
		if (liveLocationTracker != null && liveLocationTracker.isRunning) {
			liveLocationTracker.stopLocationUpdates();
		}
	}
    
    
	
}