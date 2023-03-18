package com.alcorross;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
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
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(2);
        service.scheduleWithFixedDelay(Listener.getListenerInstance(), 1, 5, TimeUnit.MILLISECONDS);
        service.scheduleWithFixedDelay(TimeoutCleaner.getTimeCleanInstance(), 10, 10, TimeUnit.SECONDS);
    }
}
