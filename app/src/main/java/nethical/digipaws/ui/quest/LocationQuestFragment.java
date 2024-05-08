package nethical.digipaws.ui.quest;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.view.Window;
import android.widget.Toast;
import java.util.ArrayList;
import android.os.Bundle;
import nethical.digipaws.utils.SurvivalModeConfig;
import org.osmdroid.api.IMapController;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.overlay.Marker;
import android.view.ViewGroup;
import androidx.preference.PreferenceManager;
import org.osmdroid.config.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import nethical.digipaws.R;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import nethical.digipaws.utils.LocationHelper;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.MapView;

public class LocationQuestFragment extends Fragment {
  private MapView map = null;
  private Button start;
  private TextView countdownText;
  private CountDownTimer timer;
  private boolean isQuestRunning = false;
  private Location radarLocation;

  private boolean isZoomed = false; // checks if map has been zoomed to current location
  LocationHelper liveLocationTracker = null;

  private MyLocationNewOverlay mLocationOverlay;

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
        .load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()));
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

    timer =
        new CountDownTimer(startTime, interval) {
          @Override
          public void onTick(long millisUntilFinished) {
            // Update the TextView with remaining time
            countdownText.setText("Time Remaining: " + formatTime(millisUntilFinished));
          }

          @Override
          public void onFinish() {
            countdownText.setText("Time Up!");
            liveLocationTracker.stopLocationUpdates();
            isQuestRunning = false;
          }
        };

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
                            new GeoPoint(location.getLatitude(), location.getLongitude());

                        // Use the location data here
                        myLocation.setPosition(currentLocation);

                        if (isQuestRunning) {
                          double distance =
                              liveLocationTracker.getDistanceBetweenLocations(
                                  location, radarLocation);

                          if (distance > 100) {
                            Toast.makeText(
                                    requireContext(),
                                    "Quest Completed " + String.valueOf(distance),
                                    Toast.LENGTH_LONG)
                                .show();
                            SurvivalModeConfig.stop(requireContext());
                          }
                        }
                      }
                    }
                  });
              liveLocationTracker.startLocationUpdates();
            })
        .start();

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
              double radius = 100; // Radius in meters

              Polygon circlePolygon = drawCircle(map, latitude, longitude, radius);
              map.getOverlays().add(circlePolygon);
              radarLocation = location;
            }
          }
        });
    locationHelperCircle.startLocationUpdates();

    start.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (isQuestRunning) {

              isQuestRunning = false;
              start.setText("Start");
              timer.cancel();
              countdownText.setText("Time: 5mins");
              liveLocationTracker.stopLocationUpdates();
              return;
            }
            isQuestRunning = true;
            timer.start();
            start.setText("Stop Quest");
          }
        });
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
