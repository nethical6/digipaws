package nethical.digipaws.fragments.quests;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import nethical.digipaws.R;
import nethical.digipaws.fragments.dialogs.LoadingDialog;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DigiConstants;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;

import nethical.digipaws.utils.LocationManager;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.MapView;

public class MarathonQuest extends Fragment {

    private MapView mapView;
    private Button btnStartQuest;

    private boolean isQuestRunning;

    private SharedPreferences questPref;

    private LocationManager liveLocationTracker = null;

    private final Location radarLocation = new Location("radar");
    private int radarRadius = 0; // the size of radar

    private final LoadingDialog loadingDialog = new LoadingDialog("Calculating Location...");
    private Marker myLocation;

    private SharedPreferences lvData;

    private static final int REQUEST_FINE_LOCATION_PERMISSION = 69;
    private static final int REQUEST_POST_NOTIFICATIONS_PERMISSION = 70;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.marathon_quest_layout, container, false);
        mapView = view.findViewById(R.id.map);
        btnStartQuest = view.findViewById(R.id.start_location_quest);

        EdgeToEdge.enable(requireActivity());

        MaterialCardView cardView = view.findViewById(R.id.bottom_card);

        // apply cardview ui configurations
        ColorStateList backgroundColor = cardView.getCardBackgroundColor();
        int backgroundColorValue = backgroundColor.getDefaultColor();
        int semiTransparentColor = ColorUtils.setAlphaComponent(backgroundColorValue, (int) (0.65 * 255));
        cardView.setCardBackgroundColor(semiTransparentColor);

        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingDialog.show(getParentFragmentManager(), "loading_dialog");

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        myLocation = new Marker(mapView);
        mapView.getOverlays().add(myLocation);

        checkLocationPermission();

        lvData = requireContext().getSharedPreferences("lvData", Context.MODE_PRIVATE);
        radarRadius = lvData.getInt("marathon_quest_distance", DigiConstants.RADAR_RADIUS) + DigiConstants.RADAR_RADIUS_LV_INC;


        questPref = requireContext().getSharedPreferences(DigiConstants.PREF_QUEST_INFO_FILE, Context.MODE_PRIVATE);

        // updates current location on map
        new Thread(() -> {
            liveLocationTracker = new LocationManager(requireContext());
            liveLocationTracker.setLiveLocationListener(new LocationManager.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        myLocation.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));

                        // not able to use foreground services due to google play foreground restrictions
                        if (isQuestRunning) {
                            double distance = liveLocationTracker.getDistanceBetweenLocations(location, radarLocation);
                            if (distance >= radarRadius) {
                                endQuest();
                                isQuestRunning = false;
                                showQuestCompleteDialog();
                                CoinManager.incrementCoin(requireContext());
                                questPref.edit().putInt(DigiConstants.KEY_TOTAL_DISTANCE_RUN,questPref.getInt(DigiConstants.KEY_TOTAL_DISTANCE_RUN,0)+ radarRadius).apply();
                            }
                        }
                    }
                }
            });
            liveLocationTracker.startLocationUpdates();
        }).start();

        if (questPref.getBoolean(DigiConstants.PREF_IS_MARATHON_QUEST_RUNNING_KEY, false) && questPref.contains("radius") && questPref.contains(DigiConstants.KEY_RADAR_LATITUDE) && questPref.contains(DigiConstants.KEY_RADAR_LONGITUDE)) {
            reloadActiveQuest();
        } else {
            makeRadar();
            liveLocationTracker = new LocationManager(requireContext());
        }

        btnStartQuest.setOnClickListener(v -> {
            if (!isQuestRunning) {
                startQuest();
            } else {
                endQuest();
            }
        });
    }

    private void endQuest(){
        SharedPreferences.Editor editor = questPref.edit();
        editor.putString(DigiConstants.PREF_QUEST_ID_KEY, DigiConstants.QUEST_ID_NULL);
        editor.putBoolean(DigiConstants.PREF_IS_MARATHON_QUEST_RUNNING_KEY, false);

        editor.remove("radius");
        editor.remove(DigiConstants.KEY_RADAR_LATITUDE);
        editor.remove(DigiConstants.KEY_RADAR_LONGITUDE);
        editor.apply();

        btnStartQuest.setText(R.string.start);
        isQuestRunning = false;
    }

    private void startQuest(){
        SharedPreferences.Editor editor = questPref.edit();
        editor.putString(DigiConstants.PREF_QUEST_ID_KEY, DigiConstants.QUEST_ID_MARATHON);
        editor.putBoolean(DigiConstants.PREF_IS_MARATHON_QUEST_RUNNING_KEY, true);

        editor.putInt("radius", radarRadius);
        editor.putString(DigiConstants.KEY_RADAR_LATITUDE, String.valueOf(radarLocation.getLatitude()));
        editor.putString(DigiConstants.KEY_RADAR_LONGITUDE, String.valueOf(radarLocation.getLongitude()));
        editor.apply();

        btnStartQuest.setText(R.string.stop);
        isQuestRunning = true;
    }

    private void reloadActiveQuest() {
        isQuestRunning = true;
        double latitude = Double.parseDouble(questPref.getString(DigiConstants.KEY_RADAR_LATITUDE, "0"));
        double longitude = Double.parseDouble(questPref.getString(DigiConstants.KEY_RADAR_LONGITUDE, "0"));
        GeoPoint gRadarLocation = new GeoPoint(latitude, longitude);

        IMapController controller = mapView.getController();
        controller.setZoom(18.0);
        controller.animateTo(gRadarLocation);

        radarLocation.setLatitude(latitude);
        radarLocation.setLongitude(longitude);

        addCircleTo(latitude, longitude, radarRadius);
        loadingDialog.dismiss();
        btnStartQuest.setText(R.string.stop);
    }
    private void makeRadar() {

        // capture current location for radar and then immediately close the listener
        new Thread(() -> {
            LocationManager locationHelperCircle = new LocationManager(requireContext());
            locationHelperCircle.setLiveLocationListener(new LocationManager.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {

                        GeoPoint currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

                        IMapController controller = mapView.getController();
                        controller.setZoom(18.0);
                        controller.animateTo(currentLocation);

                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();


                        radarLocation.set(location);
                        addCircleTo(latitude, longitude, radarRadius);
                        loadingDialog.dismiss();
                        locationHelperCircle.stopLocationUpdates();
                    }
                }
            });
            locationHelperCircle.startLocationUpdates();
        }).start();
    }

    private void addCircleTo(double latitude, double longitude, double radius) {
        Polygon circlePolygon = drawCircle(mapView, latitude, longitude, radius);
        mapView.getOverlays().add(circlePolygon);
    }

    public Polygon drawCircle(MapView map, double latitude, double longitude, double radius) {

        GeoPoint centerPoint = new GeoPoint(latitude, longitude);

        ArrayList<GeoPoint> circlePoints = new ArrayList<>();

        // Loop to calculate points
        for (float angle = 0; angle <= 360; angle += 1.0f) {
            GeoPoint newPoint = centerPoint.destinationPoint(radius, angle);
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

        if (liveLocationTracker != null) {
            liveLocationTracker.startLocationUpdates();
        } else {
            checkLocationPermission();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (liveLocationTracker != null) {
            liveLocationTracker.stopLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if (liveLocationTracker != null) {
            liveLocationTracker.stopLocationUpdates();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (liveLocationTracker != null) {
            liveLocationTracker.stopLocationUpdates();
        }
    }

    private void checkNotificationPermision() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            loadingDialog.dismiss();
            makeNotificationPermissionDialog().create().show();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            loadingDialog.dismiss();
            makeLocationPermissionDialog().create().show();
        } else {
            checkNotificationPermision();
        }
    }

    private MaterialAlertDialogBuilder makeNotificationPermissionDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.missing_permission).setMessage(R.string.notification_notif_permission).setNeutralButton("Provide", (dialog, which) -> {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_POST_NOTIFICATIONS_PERMISSION);
        });
        return builder;
    }

    private MaterialAlertDialogBuilder makeLocationPermissionDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.missing_permission).setMessage(R.string.notification_location_permission).setNeutralButton("Provide", (dialog, which) -> {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION_PERMISSION);
        });
        return builder;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_FINE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkNotificationPermision();
                // Permission granted for ACCESS_FINE_LOCATION
                //   makeRadar();
            }
        } else if (requestCode == REQUEST_POST_NOTIFICATIONS_PERMISSION) {
            loadingDialog.show(getParentFragmentManager(), "loading_dialog"); // Use a unique tag for the dialog
            makeRadar();
        }
    }

    private void showQuestCompleteDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext()).setTitle("Quest Complete").setMessage("You earned 1 Aura point. Your next Touch Grass target is " + String.valueOf(radarRadius + 50) + " metres away!").setNeutralButton("Quit", (dialog, which) -> {
            dialog.dismiss();
            getParentFragmentManager().popBackStack();
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        lvData.edit().putInt("marathon_quest_distance", radarRadius).apply();
    }
}
