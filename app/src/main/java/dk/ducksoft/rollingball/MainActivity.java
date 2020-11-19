package dk.ducksoft.rollingball;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SensorManager sm = null;
    TextView sensorInfo = null;
    List list;
    Animation aniRotate;
    ImageView img;
    Animation Rotate;
    float[] values = new float[4];

    SensorEventListener sel = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            values = event.values;
            sensorInfo.setText("x: " + values[0] + "\ny: " + values[1] + "\nz: " + values[2]);
            if (values[0] >= -9.5) {
                Rotate.setDuration(100);
                while (values[0] > 1.5 && Rotate.hasStarted()) {
                img.startAnimation(Rotate);
                    Log.e("Animation loop0","Is running");
                }
            } else {
                img.startAnimation(aniRotate);
                aniRotate.setDuration(100);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView) findViewById(R.id.imageView);
        aniRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
        Rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anticlockwise);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorInfo = (TextView) findViewById(R.id.sensorInfo);

        list = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (list.size() > 0) {
            sm.registerListener(sel, (Sensor) list.get(0), SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            Toast.makeText(getBaseContext(), "Error: No Accelerometer.", Toast.LENGTH_LONG).show();
        }

    }

    protected void onStop() {
        if (list.size() > 0) {
            sm.unregisterListener(sel);
        }
        super.onStop();
    }
}