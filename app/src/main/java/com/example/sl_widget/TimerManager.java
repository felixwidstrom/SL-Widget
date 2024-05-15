package com.example.sl_widget;

import java.util.Timer;
import java.util.TimerTask;

public class TimerManager {
    private static TimerManager instance;
    private Timer timer;
    private boolean isTimerActive;

    private TimerManager() {
        this.timer = new Timer();
        this.isTimerActive = false;
    }

    public static synchronized TimerManager getInstance() {
        if (instance == null) {
            instance = new TimerManager();
        }
        return instance;
    }

    public void startTimer(TimerTask task, long delay, long period) {
        if (isTimerActive) {
            stopTimer();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(task, delay, period);
        isTimerActive = true;
    }

    public void stopTimer() {
        if (isTimerActive) {
            timer.cancel();
            timer.purge();
            isTimerActive = false;
        }
    }

    public boolean isTimerActive() {
        return isTimerActive;
    }
}
