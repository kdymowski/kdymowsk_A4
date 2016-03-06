package intelligence_core.kdymowsk_a4;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by intelligence-core on 3/4/16.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    /**
     * The main screen view object for the simulation.
     */
    SimView mSimView;

    /**
     * Models and simulates the motion.
     */
    Simulator mSimulator;

    /**
     * Timer is used to schedule periodic tasks.
     */
    Timer mTimer;

    /**
     * The task to be performed when the timer times out.
     */
    TimerTask mTimerTask;

    /**
     * The handler passes "runnable" objects to a background thread.
     */
    Handler mHandler = new Handler();

    SensorManager mSensorManager;

    ActionBar actionBar;// = getSupportActionBar();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);

        // This view is the main screen.
        FrameLayout mainView = (FrameLayout) findViewById(R.id.main_view);

        // Create the simulation view and add it to the main screen.
        mSimView = new SimView(this);
        mainView.addView(mSimView);

        // Get screen dimensions (in pixels).
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        // Create the simulator, passing in the size of the screen.
        mSimulator = new Simulator(screenWidth, screenHeight);

        // Set the ball radius to be some fraction of the screen size.
        int ballRadius = screenWidth/30;

        // Set the ball radius in the simulator and initialize its position.
        mSimulator.setBallRadius(ballRadius);
        PointF ballPos = new PointF(screenWidth/2, screenHeight/2);
        mSimulator.setBallPos(ballPos);

        // Get sensor manager and register the listener to it.
        mSensorManager = ((SensorManager)getSystemService(Context.SENSOR_SERVICE));
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(sensorEventListener,
                accelerometer,
                SensorManager.SENSOR_DELAY_UI);


    }


    // Define the sensor event listener.
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            PointF accel = new PointF(event.values[0], event.values[1]);
            mSimulator.setDeviceAccel(accel);
            //Log.d(TAG, "onSensorChanged() " + event.toString());
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // ignore
        }
    };

    @Override
    public void onResume()  {
        Log.d(TAG, "onResume() called");

        // Create the timer and its task.
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mSimulator.runSimulation();

                // Get ball position and radius from simulator, and pass to the view.
                mSimView.setBallPos(mSimulator.getBallPos());
                mSimView.setR(mSimulator.getBallRadius());

                // The handler will tell the background thread to redraw the view.
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mSimView.invalidate();
                    }
                });
            }
        };

        // Assign the task to the timer.
        mTimer.schedule(mTimerTask,
                10,     // delay (ms) before task is to be executed
                10);    // time (ms) between successive task executions

        super.onResume();
    }

    // Unregister sensor listener so it doesn't keep sending data.
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
        mSensorManager.unregisterListener(sensorEventListener);
    }
}

