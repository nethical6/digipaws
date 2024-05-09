package nethical.digipaws.ui.quest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import nethical.digipaws.Constants;
import nethical.digipaws.R;
import nethical.digipaws.services.LocationTrackerService;
import nethical.digipaws.utils.LocationHelper;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class LocationQuestFragment extends Fragment {
    private MapView map = null;
    private Button start;
    private TextView countdownText;
    private boolean isQuestRunning = false;
    private Location radarLocation;

    private boolean isZoomed = false; // checks if map has been zoomed to current location
    private LocationHelper liveLocationTracker = null;

    private MyLocationNewOverlay mLocationOverlay;

    private SharedPreferences locationPreference;

    public LocationQuestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();

        if (liveLocationTracker != null && liveLocationTracker.isRunning) {
            liveLocationTracker.startLocationUpdates();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (liveLocationTracker != null && !liveLocationTracker.isRunning) {
            liveLocationTracker.stopLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();

        if (liveLocationTracker != null && !liveLocationTracker.isRunning) {
            liveLocationTracker.stopLocationUpdates();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (liveLocationTracker != null && !liveLocationTracker.isRunning) {
            liveLocationTracker.stopLocationUpdates();
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_location_quest, container, false);
        Configuration.getInstance()
                .load(
                        requireContext(),
                        PreferenceManager.getDefaultSharedPreferences(requireContext()));
        start = rootView.findViewById(R.id.start_location_quest);
        map = rootView.findViewById(R.id.map);
        countdownText = rootView.findViewById(R.id.countdown_timer_loc);
        resetStatusBarColor();
        return rootView;
    }

    @Override
    public void onViewCreated(View arg0, Bundle arg1) {
        super.onViewCreated(arg0, arg1);

        Context ctx = requireContext();

        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setMultiTouchControls(true);

        Marker myLocation = new Marker(map);
        map.getOverlays().add(myLocation);

        long startTime = 5 * 60 * 1000; // 5 minutes in milliseconds
        long interval = 1000; // Update every second

        locationPreference =
                requireContext()
                        .getSharedPreferences(Constants.LOC_QUEST_DATA_PREF, Context.MODE_PRIVATE);

        new Thread(
                        () -> {
                            // Run code on background thread
                            liveLocationTracker = new LocationHelper(requireContext());

                            liveLocationTracker.setLiveLocationListener(
                                    new LocationHelper.LocationListener() {
                                        @Override
                                        public void onLocationChanged(Location location) {
                                            if (location != null) {
                                                GeoPoint currentLocation =
                                                        new GeoPoint(
                                                                location.getLatitude(),
                                                                location.getLongitude());

                                                // Use the location data here
                                                myLocation.setPosition(currentLocation);
                                            }
                                        }
                                    });
                            liveLocationTracker.startLocationUpdates();
                        })
                .start();
        makeRadar();
        /*
        if(locationPreference.getBoolean("is_radar_drawn",false)){
        	float latitude = locationPreference.getFloat("radar_latitude",0.0f);
        	float longitude = locationPreference.getFloat("radar_longitude",0.0f);
        	radarLocation.setLatitude(latitude);
        	radarLocation.setLongitude(longitude);
        	addCircleTo(radarLocation.getLatitude(),radarLocation.getLongitude(),100);
        }else{
        	makeRadar();


        }

        if(locationPreference.getBoolean("is_quest_running",false)){
        	start.setText("Stop Quest");
        	isQuestRunning=true;

        }*/

        start.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isQuestRunning) {

                            isQuestRunning = false;
                            start.setEnabled(true);
                            return;
                        }

                        isQuestRunning = true;
                        start.setEnabled(false);
                        start.setVisibility(View.INVISIBLE); // todo: animate it to fade away

                        locationPreference
                                .edit()
                                .putFloat(
                                        Constants.LOC_QUEST_RADAR_LAT_PREF,
                                        (float) radarLocation.getLatitude())
                                .apply();
                        locationPreference
                                .edit()
                                .putFloat(
                                        Constants.LOC_QUEST_RADAR_LON_PREF,
                                        (float) radarLocation.getLongitude())
                                .apply();
                        locationPreference.edit().putBoolean("is_quest_running", true).apply();
                        locationPreference.edit().putBoolean("is_radar_drawn", true).apply();

                        Intent serviceIntent =
                                new Intent(requireContext(), LocationTrackerService.class);
                        ContextCompat.startForegroundService(requireContext(), serviceIntent);
                    }
                });
    }

    private void makeRadar() {
        LocationHelper locationHelperCircle = new LocationHelper(requireContext());

        locationHelperCircle.setLiveLocationListener(
                new LocationHelper.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {

                            GeoPoint currentLocation =
                                    new GeoPoint(location.getLatitude(), location.getLongitude());
                            locationHelperCircle.stopLocationUpdates();

                            IMapController controller = map.getController();
                            controller.setZoom(17.0);
                            controller.animateTo(currentLocation);

                            isZoomed = false;

                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            double radius = Constants.LOC_QUEST_RADAR_RADIUS; // Radius in meters

                            addCircleTo(latitude, longitude, radius);
                            radarLocation = location;
                        }
                    }
                });
        locationHelperCircle.startLocationUpdates();
    }

    private void addCircleTo(double latitude, double longitude, double radius) {
        Polygon circlePolygon = drawCircle(map, latitude, longitude, radius);
        map.getOverlays().add(circlePolygon);
    }

    private void resetStatusBarColor() {
        // Get the hosting activity and reset status bar color as well as the navigation bar color
        Window window = requireActivity().getWindow();

        // Set the background color of the window
        window.setBackgroundDrawableResource(
                R.color.app_background_color); // Clear existing background (optional)
        window.setStatusBarColor(R.color.app_background_color); // Set status bar color (optional)
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
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

        Polygon circlePolygon = new Polygon(map); // Use org.osmdroid.bonuspack.overlays.Polygon

        circlePolygon.setPoints(circlePoints);

        circlePolygon.setFillColor(Color.argb(64, 255, 0, 0)); // Semi-transparent blue
        circlePolygon.setStrokeColor(Color.TRANSPARENT);
        return circlePolygon;
    }

    private String formatTime(long millisUntilFinished) {
        long seconds = millisUntilFinished / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
