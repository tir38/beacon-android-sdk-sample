package net.helpscout.sample.beacon;

import android.app.Application;

import com.helpscout.beacon.Beacon;
import com.helpscout.beacon.ui.BeaconEventLifecycleHandler;
import com.helpscout.beacon.ui.BeaconOnClosedListener;
import com.helpscout.beacon.ui.BeaconOnOpenedListener;

import timber.log.Timber;

public class CustomisationApplication extends Application {

    private static String secureBeaconId = "598349f2-c5a4-424d-8eb6-8c1d0813a42f";
    private Scratch scratch;

    @Override
    public void onCreate() {
        super.onCreate();
        initBeacon();

        // Use this to start listening to close and open events on the Beacon
        initBeaconListener();

        scratch = new Scratch(this.getApplicationContext());
    }

    private void initBeacon() {
        new Beacon.Builder()
                .withBeaconId(secureBeaconId)
                .withLogsEnabled(true)
                .build();
    }

    public Scratch getScratch() {
        return scratch;
    }

    private String getBeaconColor() {
        return "#FF0022";
    }

    private void initBeaconListener() {
        BeaconEventLifecycleHandler eventLifecycleHandler = new BeaconEventLifecycleHandler(
                new BeaconOnOpenedListener() {
                    @Override
                    public void onOpened() {
                        Timber.i("Open Beacon event called");
                    }
                },
                new BeaconOnClosedListener() {
                    @Override
                    public void onClosed() {
                        Timber.i("Close Beacon event called");
                    }
                }
        );

        registerActivityLifecycleCallbacks(eventLifecycleHandler);
    }

}
