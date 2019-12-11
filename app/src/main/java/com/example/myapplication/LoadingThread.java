package com.example.myapplication;

import android.os.Handler;
import android.os.Looper;

public class LoadingThread extends Thread {
    Handler handler;
    @Override
    public void run() {
        super.run();
        Looper.prepare();
        handler = new Handler();
        Looper.loop();

    }
}
