package com.example.root.slidecontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.widget.ListView;

import java.util.ArrayList;

public class IpSettingsActivity extends AppCompatActivity {

    private ArrayList<IpClass> ips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_settings);
        ListView listView = (ListView) findViewById(R.id.ipListView);
        DBhelper dBhelper = new DBhelper(this);

        ips = dBhelper.getAllIps();

        CustomAdapter customAdapter = new CustomAdapter(this, ips);
        listView.setAdapter(customAdapter);

    }
}
