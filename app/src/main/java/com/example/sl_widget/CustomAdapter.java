package com.example.sl_widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public CustomAdapter(Context context, String[] values) {
        super(context, R.layout.list_item_layout, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.list_item_layout, parent, false);

        TextView textView = view.findViewById(R.id.list_text);
        Button bus_button = view.findViewById(R.id.list_button_bus);
        Button metro_button = view.findViewById(R.id.list_button_metro);
        Button train_button = view.findViewById(R.id.list_button_train);
        Button tram_button = view.findViewById(R.id.list_button_tram);
        Button ship_button = view.findViewById(R.id.list_button_ship);

        String text = values[position].split(":")[0];
        String siteId = values[position].split(":")[1];

        textView.setText(text);
        bus_button.setOnClickListener(x -> update(siteId + ":" + "BUS"));
        metro_button.setOnClickListener(x -> update(siteId + ":" + "METRO"));
        train_button.setOnClickListener(x -> update(siteId + ":" + "TRAIN"));
        tram_button.setOnClickListener(x -> update(siteId + ":" + "TRAM"));
        ship_button.setOnClickListener(x -> update(siteId + ":" + "SHIP"));

        return view;
    }

    private void update(String data) {
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(WidgetProvider.ACTION_UPDATE_WIDGET);
        intent.putExtra(WidgetProvider.EXTRA_WIDGET_DATA, data);
        context.sendBroadcast(intent);
    }
}
