package com.alcorross;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LaunchManager implements Runnable {

    private static LaunchManager launchManagerInstance;

    private LaunchManager() {
    }

    public static LaunchManager getLaunchManagerInstance() {
        if (launchManagerInstance == null) launchManagerInstance = new LaunchManager();
        return launchManagerInstance;
    }

    @Override
    public void run() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(Listener.getListenerInstance(), 1, 5, TimeUnit.MILLISECONDS);
        service.scheduleWithFixedDelay(TimeoutCleaner.getTimeCleanInstance(), 10, 5, TimeUnit.SECONDS);
    }
}
