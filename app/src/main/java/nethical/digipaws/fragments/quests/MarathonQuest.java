package nethical.digipaws.fragments.quests;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import nethical.digipaws.R;
import nethical.digipaws.fragments.dialogs.LoadingDialog;
import nethical.digipaws.services.LocationTrackerService;
import nethical.digipaws.utils.DigiConstants;
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
		
	
		loadingDialog.show(getActivity().getSupportFragmentManager(), "loading_dialog"); // Use a unique tag for the dialog
		
		
		mapView.setTileSource(TileSourceFactory.MAPNIK);
		
		mapView.setMultiTouchControls(true);
		
		Marker myLocation = new Marker(mapView);
		
		mapView.getOverlays().add(myLocation);
		
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
		
		
		makeRadar();
		
        startQuest.setOnClickListener(v -> {
                
                
            Intent serviceIntent = new Intent(requireContext(), LocationTrackerService.class);
            serviceIntent.putExtra(DigiConstants.KEY_RADAR_LATITUDE,radarLocation.getLatitude());
            serviceIntent.putExtra(DigiConstants.KEY_RADAR_LONGITUDE,radarLocation.getLongitude());
            requireContext().startForegroundService(serviceIntent);
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
	
	
	
	
	// Layout file (fragment_my.xml)
	public static int getLayoutResourceId() {
		return R.layout.marathon_quest_layout;
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