package nethical.digipaws.fragments.quests;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.internal.EdgeToEdgeUtils;
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
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.MapView;

public class MarathonQuest extends Fragment {

    private MapView mapView;
    private Button startQuest;

    private boolean isRunning;
    private boolean isRadarDrawn = false;

    private SharedPreferences questPref;

    private LocationManager liveLocationTracker = null;

    private Location radarLocation = new Location("radar");
    private int travelRadius = 0; // the size of radar
    
    private MyLocationNewOverlay mLocationOverlay;
    private LoadingDialog loadingDialog = new LoadingDialog("Calculating Location...");
    private Marker myLocation;

    
    private SharedPreferences lvData;
    
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 69;
    private static final int REQUEST_POST_NOTIFICATIONS_PERMISSION = 70;
    
    
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.marathon_quest_layout, container, false);
        mapView = view.findViewById(R.id.map);
        startQuest = view.findViewById(R.id.start_location_quest);

        EdgeToEdgeUtils.applyEdgeToEdge(getActivity().getWindow(), true);

        MaterialCardView cardView = view.findViewById(R.id.bottom_card);

        ColorStateList backgroundColor = cardView.getCardBackgroundColor();
        int defaultColor = Color.WHITE; // Default color in case background color is null

        // Get the default background color of the CardView
        int backgroundColorValue =
                backgroundColor != null ? backgroundColor.getDefaultColor() : defaultColor;

        // Adjust the alpha to create a semi-transparent color (65% transparency)
        int semiTransparentColor =
                ColorUtils.setAlphaComponent(backgroundColorValue, (int) (0.65 * 255));

        // Find the CardView and set the background color
        cardView.setCardBackgroundColor(semiTransparentColor);

        Configuration.getInstance()
                .load(
                        requireContext(),
                        PreferenceManager.getDefaultSharedPreferences(requireContext()));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingDialog.show(
                getActivity().getSupportFragmentManager(),
                "loading_dialog"); // Use a unique tag for the dialog

        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.setMultiTouchControls(true);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        myLocation = new Marker(mapView);

        mapView.getOverlays().add(myLocation);

        checkLocationPermission();
        
        lvData = requireContext().getSharedPreferences("lvData",Context.MODE_PRIVATE);
        travelRadius = lvData.getInt("marathon_quest_distance",DigiConstants.RADAR_RADIUS) + DigiConstants.RADAR_RADIUS_LV_INC;
        
        
        questPref =
                requireContext()
                        .getSharedPreferences(
                                DigiConstants.PREF_QUEST_INFO_FILE, Context.MODE_PRIVATE);

        
        if (!questPref.getBoolean(DigiConstants.PREF_IS_QUEST_RUNNING_KEY, false))  {
                makeRadar();
                isRadarDrawn = true;

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
            } 

            startQuest.setOnClickListener(
                    v -> {
                        if (!isRunning) {

                            SharedPreferences.Editor editor = questPref.edit();
                            editor.putString(
                                    DigiConstants.PREF_QUEST_ID_KEY,
                                    DigiConstants.QUEST_ID_MARATHON);
                            editor.putBoolean(DigiConstants.PREF_IS_QUEST_RUNNING_KEY, true);
                            editor.apply();

                            startQuest.setText(R.string.stop);
                            isRunning = true;
                            Intent serviceIntent =
                                    new Intent(requireContext(), LocationTrackerService.class);
                            serviceIntent.putExtra(
                                    DigiConstants.KEY_RADAR_LATITUDE, radarLocation.getLatitude());
                            serviceIntent.putExtra(
                                    DigiConstants.KEY_RADAR_LONGITUDE,
                                    radarLocation.getLongitude());
                            serviceIntent.putExtra("radius",travelRadius);
                            serviceIntent.setAction("START");
                            requireContext().startForegroundService(serviceIntent);
                            
                        } else {
                            startQuest.setText(R.string.start);
                            isRunning = false;
                            Intent serviceIntent =
                                    new Intent(requireContext(), LocationTrackerService.class);
                            serviceIntent.setAction("STOP");
                            requireContext().startForegroundService(serviceIntent);
                            
                        }
                    });
        }

    private void makeRadar() {

        // capture current location for radar and then immediately close the listener
        new Thread(
                        () -> {
                            LocationManager locationHelperCircle =
                                    new LocationManager(requireContext());
                            locationHelperCircle.setLiveLocationListener(
                                    new LocationManager.LocationListener() {
                                        @Override
                                        public void onLocationChanged(Location location) {
                                            if (location != null) {

                                                GeoPoint currentLocation =
                                                        new GeoPoint(
                                                                location.getLatitude(),
                                                                location.getLongitude());

                                                IMapController controller = mapView.getController();
                                                controller.setZoom(18.0);
                                                controller.animateTo(currentLocation);

                                                double latitude = location.getLatitude();
                                                double longitude = location.getLongitude();
                                                 
                                
                                                radarLocation.set(location);
                                                addCircleTo(latitude, longitude,travelRadius);
                                                loadingDialog.dismiss();
                                                locationHelperCircle.stopLocationUpdates();
                                            }
                                        }
                                    });
                            locationHelperCircle.startLocationUpdates();
                        })
                .start();
    }

    private void addCircleTo(double latitude, double longitude, double radius) {
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

        if (liveLocationTracker != null) {
            liveLocationTracker.startLocationUpdates();
        } else {
            checkLocationPermission();
        }
        requireContext().registerReceiver(locUpdateReceiver, new IntentFilter("LOCATION_UPDATES"));
        requireContext().registerReceiver(questComplete, new IntentFilter("MARATHON_QUEST_COMPLETE"));
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
        requireContext().unregisterReceiver(locUpdateReceiver);
        requireContext().unregisterReceiver(questComplete);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (liveLocationTracker != null) {
            liveLocationTracker.stopLocationUpdates();
        }
    }

    private void checkNotificationPermision() {
        if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            loadingDialog.dismiss();
            makeNotificationPermissionDialog().create().show();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            loadingDialog.dismiss();
            makeLocationPermissionDialog().create().show();
        } else {
            checkNotificationPermision();
        }
    }

    private MaterialAlertDialogBuilder makeNotificationPermissionDialog() {
        MaterialAlertDialogBuilder builder =
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.missing_permission)
                        .setMessage(R.string.notification_notif_permission)
                        .setNeutralButton(
                                "Provide",
                                (dialog, which) -> {
                                    requestPermissions(
                                            new String[] {Manifest.permission.POST_NOTIFICATIONS},
                                            REQUEST_POST_NOTIFICATIONS_PERMISSION);
                                });
        return builder;
    }

    private MaterialAlertDialogBuilder makeLocationPermissionDialog() {
        MaterialAlertDialogBuilder builder =
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.missing_permission)
                        .setMessage(R.string.notification_location_permission)
                        .setNeutralButton(
                                "Provide",
                                (dialog, which) -> {
                                    requestPermissions(
                                            new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                            REQUEST_FINE_LOCATION_PERMISSION);
                                });
        return builder;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_FINE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkNotificationPermision();
                // Permission granted for ACCESS_FINE_LOCATION
                //   makeRadar();
            }
        } else if (requestCode == REQUEST_POST_NOTIFICATIONS_PERMISSION) {
            loadingDialog.show(
                    getActivity().getSupportFragmentManager(),
                    "loading_dialog"); // Use a unique tag for the dialog
            makeRadar();
        }
    }

    private BroadcastReceiver locUpdateReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals("LOCATION_UPDATES")) {
                        Double lat = intent.getDoubleExtra("clatitude",0D);
                        Double lon = intent.getDoubleExtra("clongitude",0D);
                        
                        GeoPoint crntLoc = new GeoPoint(0D,0D);
                        crntLoc.setLatitude(lat);
                        crntLoc.setLongitude(lon);
                        if(myLocation!=null){
                            myLocation.setPosition(crntLoc);
                        }

                        if (!isRadarDrawn) {
                            Double rlat = intent.getDoubleExtra("rlatitude",0D);
                            Double rlon = intent.getDoubleExtra("rlongitude",0D);
                            radarLocation.setLatitude(rlat);
                            radarLocation.setLongitude(rlon);
                            
                            loadingDialog.dismiss();
                            addCircleTo(rlat, rlon, travelRadius);
                            IMapController controller = mapView.getController();
                            controller.setZoom(18.0);
                            GeoPoint loc = new GeoPoint(rlat, rlon);
                            controller.animateTo(loc);
                            isRunning = true;
                            isRadarDrawn = true;
                            startQuest.setText(getString(R.string.stop));
                        }
                    }
                }
            };

    private BroadcastReceiver questComplete =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals("MARATHON_QUEST_COMPLETE")) {
                        isRunning = false;
                MaterialAlertDialogBuilder builder =
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Quest Complete")
                        .setMessage("You earned 1 Aura point. Your next Touch Grass target is " + String.valueOf(travelRadius + 50 ) +" metres away!")
                        .setNeutralButton(
                                "Quit",
                                (dialog, which) -> {
                                    dialog.dismiss();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                });
                        Dialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.show();
                        lvData.edit().putInt("marathon_quest_distance",travelRadius).apply();
                    }
                }
            };

    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(locUpdateReceiver, new IntentFilter("LOCATION_UPDATES"),Context.RECEIVER_NOT_EXPORTED);
            context.registerReceiver(questComplete, new IntentFilter("MARATHON_QUEST_COMPLETE"),Context.RECEIVER_NOT_EXPORTED);
        }else {
            context.registerReceiver(locUpdateReceiver, new IntentFilter("LOCATION_UPDATES"));
            context.registerReceiver(questComplete, new IntentFilter("MARATHON_QUEST_COMPLETE"));
        }
    }
}
