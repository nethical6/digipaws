package nethical.digipaws.fragments.quests;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import nethical.digipaws.R;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class PushupQuest extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private TextView pushupCountText;
    private SharedPreferences sharedPreferences;
    
    
    private int pushupCount = 0;
    private boolean isBodyStateLow = false;
    
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pushup_quest_layout, container, false);
        pushupCountText = view.findViewById(R.id.pushup_count);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (proximitySensor == null) {
            // Handle no sensor found
            Log.d("sensor","no");
        }
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        
          if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            
            float distance = event.values[0];
          //  Toast.makeText(requireContext(),String.valueOf(distance),Toast.LENGTH_SHORT).show();
            Log.i("sensor",String.valueOf(distance));
            if(isBodyStateLow){
                if(distance>40){
                    isBodyStateLow=false;
                }else{
                    return;
                }
            }
            
            
            if(distance<15){
                pushupCount++;
                updatePushUpCount(pushupCount);
                isBodyStateLow=true;
            }
        }
        
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {}
    
    private void updatePushUpCount(int count) {
        // Update UI element (TextView) to display current push-up count
        pushupCountText.setText(String.valueOf(count));
    }
    
        @Override
    public void onResume() {
        super.onResume();
       if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
           }
    
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

}
