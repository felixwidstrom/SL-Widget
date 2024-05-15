package com.example.sl_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.*;

public class WidgetProvider extends AppWidgetProvider {

    public static final String ACTION_UPDATE_WIDGET = "com.example.sl_widget.UPDATE_WIDGET";
    public static final String EXTRA_WIDGET_DATA = "com.example.sl_widget.WIDGET_DATA";

    private Timer timer = new Timer();
    private boolean active = false;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_UPDATE_WIDGET.equals(intent.getAction())) {
            String data = intent.getStringExtra(EXTRA_WIDGET_DATA);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, WidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
            TimerManager timerManager = TimerManager.getInstance();
            TimerTask update = updateAppWidget(context, appWidgetManager, appWidgetIds, data);
            timerManager.startTimer(update, 100, 60000);
        }
    }

    private TimerTask updateAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String data){
        return new TimerTask() {
            @Override
            public void run() {
                String[] deps;
                try {
                    deps = Utility.getDepartures(data.split(":")[0], data.split(":")[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                if (deps.length == 0) {
                    views.setTextViewText(R.id.main_text, "---");
                    views.setTextViewText(R.id.sub_text, "No departures found");
                    views.setTextViewText(R.id.main_time, "- min");
                } else if (deps.length == 1) {
                    views.setTextViewText(R.id.main_text, deps[0].split(":")[0]);
                    views.setTextViewText(R.id.main_time, deps[0].split(":")[1]);
                    views.setTextViewText(R.id.sub_text, "");
                } else {
                    views.setTextViewText(R.id.main_text, deps[0].split(":")[0]);
                    views.setTextViewText(R.id.main_time, deps[0].split(":")[1]);
                    StringBuilder temp = new StringBuilder();
                    for (int i = 0; i < Math.min(deps.length, 5); i++) {
                        temp.append(deps[i].replace(":", " ")).append(" ");
                    }
                    views.setTextViewText(R.id.sub_text, temp.toString());
                }

                for (int appWidgetId : appWidgetIds) {
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }
            }
        };
    }
}
