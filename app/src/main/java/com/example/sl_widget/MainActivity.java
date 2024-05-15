package com.example.sl_widget;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_layout);

        EditText searchQuery = findViewById(R.id.search_query);
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(view -> {
            String query = searchQuery.getText().toString();
            try {
                String[] temp = Utility.getStops();
                ListView listView = findViewById(R.id.list_view);
                CustomAdapter adapter = new CustomAdapter(this, Utility.filter(temp, query));
                listView.setAdapter(adapter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
